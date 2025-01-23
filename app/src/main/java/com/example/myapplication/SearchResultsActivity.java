package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchResultsActivity extends AppCompatActivity {

    EditText searchtxt;
    ImageView resultimg;
    TextView resulttxt;
    String search, type;

    DatabaseReference searchReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        searchReference = FirebaseDatabase.getInstance().getReference("search");

        UIInitialize();

        searchtxt = findViewById(R.id.SearchInputtxt);
        resultimg = findViewById(R.id.resultPhoto);
        resulttxt = findViewById(R.id.result);

        Intent intent = getIntent();
        if(intent.hasExtra("Message")){
            search = intent.getStringExtra("Message");
        }
        if(intent.hasExtra("Type")){
            type = intent.getStringExtra("Type");
        }

        if("strategy".equals(type)) {
            search(search);
        }else if("search".equals(type)){
            if(search.equals("Glass Bottle")){
                searchtxt.setText(search);
                resultimg.setImageResource(R.drawable.bottle);
                resulttxt.setText("");
            }else{
                resultimg.setImageResource(0);
                resulttxt.setText("Undefined, Please search again!");
            }
        }

    }

    private void UIInitialize(){

        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.miInfo);

        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);


        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

        bottomNavigationView.setBackground(null);

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.miMapping)
                {
                    startActivity(new Intent(SearchResultsActivity.this,MapActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.miHome) {
                    startActivity(new Intent(SearchResultsActivity.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miInfo){
                    startActivity(new Intent(SearchResultsActivity.this,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miProfile){
                    startActivity(new Intent(SearchResultsActivity.this,ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SearchResultsActivity.this,CamActivity.class));
                overridePendingTransition(0,0);
            }
        });

        topAppBar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(SearchResultsActivity.this,NewsActivity.class));
            overridePendingTransition(0,0);
        });

        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.miGame) {
                startActivity(new Intent(SearchResultsActivity.this,GameActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else {
                return false;
            }
        });
    }

    private void search(String search){
        searchReference.child(search).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String photoPath = snapshot.child("photo").getValue(String.class);
                    String guideline = snapshot.child("guideline").getValue(String.class);

                    searchtxt.setText(search);
                    resulttxt.setText(guideline);
                    Glide.with(SearchResultsActivity.this).load(photoPath).into(resultimg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}