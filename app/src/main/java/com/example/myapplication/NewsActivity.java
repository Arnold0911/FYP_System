package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.DataType.news;
import com.example.myapplication.DataType.user;
import com.example.myapplication.adapter.friendAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.example.myapplication.adapter.newsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    RecyclerView newsRecycler;
    newsAdapter newsAdapter;
    List<news> newsList = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference("user");

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        newsRecycler = findViewById(R.id.newsRecycler);

        String userId = currentUser.getUid();

        DatabaseReference newsReference = usersReference.child(userId).child("news");

        newsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String newsId = snapshot.getKey();

                    newsReference.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            news news = new news(snapshot.child("name").getValue(String.class), snapshot.child("context").getValue(String.class));


                            newsList.add(news);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NewsActivity.this, RecyclerView.VERTICAL, false);
                            newsRecycler.setLayoutManager(layoutManager);
                            newsAdapter = new newsAdapter(NewsActivity.this, newsList);
                            newsRecycler.setAdapter(newsAdapter);
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


        UIInitialize();
    }

    private void UIInitialize(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);


        topAppBar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.setting) {
                    Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                    return true;
                } else if (id == R.id.Scan) {
                    Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                    return true;
                } else if (id == R.id.buttonLogout) {
                    Toast.makeText(getApplicationContext(), "Item 3 Selected", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }
}