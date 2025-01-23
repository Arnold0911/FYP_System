package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataType.user;
import com.example.myapplication.adapter.friendAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 123;

    ImageView userIcon;
    TextView username, description, edit;
    RecyclerView friendRecycler;
    friendAdapter friendAdapter;
    List<user> friendsList = new ArrayList<>();
    List<String> friendsUID;
    FirebaseAuth auth;
    DatabaseReference usersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference("user");

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        UIInitialize();

        showUser();

        findFriend();


        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//                Intent refresh = new Intent(ProfileActivity.this, ProfileActivity.class);
//                startActivity(refresh);
            }
        });

        description = findViewById(R.id.description);
        edit = findViewById(R.id.edit);

        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                description.setVisibility(View.GONE);
                EditText editText = new EditText(ProfileActivity.this);
                editText.setText(description.getText());
                // add editText to the same LayoutParams as textView
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout layout = (LinearLayout) findViewById(R.id.linear); // the id of your LinearLayout
                layout.addView(editText , params);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = editText.getText().toString();
                        description.setText(text);
                        description.setVisibility(View.VISIBLE);
                        layout.removeView(editText); // remove EditText
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid());
                        userReference.child("description").setValue(text);
                    }
                });
                return false;
            }
        });
    }

    private void UIInitialize(){

        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.miProfile);

        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);


        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

        bottomNavigationView.setBackground(null);

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.miMapping)
                {
                    startActivity(new Intent(ProfileActivity.this,MapActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.miHome) {
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miInfo){
                    startActivity(new Intent(ProfileActivity.this,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miProfile){
                    startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,CamActivity.class));
                overridePendingTransition(0,0);
            }
        });

        topAppBar.setNavigationOnClickListener(view -> {
            finish();
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.setting) {
                    startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                    return true;
                } else if (id == R.id.Scan) {

                    return true;
                } else if (id == R.id.buttonLogout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    private void showUser(){
        username = findViewById(R.id.name);
        userIcon = findViewById(R.id.userIcon);
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();

        usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userImgFromDB = snapshot.child("defaultImageUrl").getValue(String.class);
                    String userName = snapshot.child("name").getValue(String.class);
                    String Desc = snapshot.child("description").getValue(String.class);

                    username.setText(userName);
                    if("".equals(Desc) || Desc.isEmpty()){
                        description.setText("You can enter some description about you");
                    }else{
                        description.setText(Desc);
                    }

                    Glide.with(ProfileActivity.this).load(userImgFromDB).into(userIcon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE) {
            if(resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_images");
                StorageReference imageReference = storageReference.child(imageUri.getLastPathSegment());
                imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageURL = uri.toString();
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid());
                                userReference.child("defaultImageUrl").setValue(imageURL);
                                Glide.with(ProfileActivity.this).load(imageURL).into(userIcon);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Upload error", e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Upload error", e.getMessage());
                    }
                });

            } else if(resultCode == RESULT_CANCELED) {
                // User cancelled the action
            }
        }
    }

    private void findFriend() {
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();

        DatabaseReference friendsReference = usersReference.child(userId).child("friend");

        friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String friendUid = snapshot.getValue(String.class);

                    usersReference.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user users = snapshot.getValue(user.class);
                            friendsList.add(users);

                            friendRecycler = findViewById(R.id.friendRecycler);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this, RecyclerView.HORIZONTAL, false);
                            friendRecycler.setLayoutManager(layoutManager);
                            friendAdapter = new friendAdapter(ProfileActivity.this, friendsList);
                            friendRecycler.setAdapter(friendAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}