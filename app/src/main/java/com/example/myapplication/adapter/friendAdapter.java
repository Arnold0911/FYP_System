package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataType.user;
import com.example.myapplication.ProfileActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class friendAdapter extends RecyclerView.Adapter<friendAdapter.friendViewHolder> {

    Context context;
    List<user> friends;

    private PopupWindow popupWindow;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    FirebaseAuth auth;

    DatabaseReference usersReference;
    public friendAdapter(Context context, List<user> friends){
        this.context = context;
        this.friends = friends;
    }


    @NonNull
    @Override
    public friendAdapter.friendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_row, parent, false);
        return new friendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendAdapter.friendViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.friendName.setText(friends.get(position).getName());
        Glide.with(context).load(friends.get(position).getDefaultImageUrl()).into(holder.friendIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static final class friendViewHolder extends RecyclerView.ViewHolder{
        ImageView friendIcon;
        TextView friendName;

        public friendViewHolder(@NonNull View itemView){
            super(itemView);
            friendIcon = itemView.findViewById(R.id.friendIcon);
            friendName = itemView.findViewById(R.id.friendName);
        }
    }

    private void initPopupWindow(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
        usersReference = FirebaseDatabase.getInstance().getReference("user");
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();



        popupWindow = new PopupWindow(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);

        popupWindow.setElevation(40);

        ImageView friendIcon = view.findViewById(R.id.friendIcon);
        TextView friendName = view.findViewById(R.id.friendName);

        friendName.setText(friends.get(position).getName());
        Glide.with(context).load(friends.get(position).getDefaultImageUrl()).into(friendIcon);

        Button delBtn = view.findViewById(R.id.deletebutton);


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Delete button clicked", Toast.LENGTH_SHORT).show();
                alert = null;
                builder = new AlertDialog.Builder(v.getContext());
                alert = builder.setTitle("System alert：")
                        .setMessage("Do you want to delete your friend" + friends.get(position).getName())
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                popupWindow.dismiss();
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String friendID = friends.get(position).getUid();
//                                Toast.makeText(context, friendID, Toast.LENGTH_SHORT).show();

                                usersReference.child(userId).child("friend").child(friendID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>(){
                                    @Override
                                    public void onSuccess(Void aVoid){
                                        Toast.makeText(v.getContext(), "your friend has been removed from your friend list.", Toast.LENGTH_SHORT).show();

                                        usersReference.child(friendID).child("friend").child(userId).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>(){
                                                    @Override
                                                    public void onSuccess(Void aVoid){
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener(){
                                                    @Override
                                                    public void onFailure(@NonNull Exception e){
                                                        Toast.makeText(v.getContext(), "Delete failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                        Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                                        v.getContext().startActivity(intent);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener(){
                                    @Override
                                    public void onFailure(@NonNull Exception e){
                                        Toast.makeText(v.getContext(), "Delete failure", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).create();
                alert.show();
            }
        });

        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
