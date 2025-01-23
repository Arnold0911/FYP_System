package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchActivity extends AppCompatActivity {

    EditText input;
    ImageButton searchbtn, plastic, paper, glass, organic, electronic, metal, special, battery;
    String user = "Guest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        if(intent.hasExtra("User")){
            user = intent.getStringExtra("User");
        }
        typeButtonIntialize();

        input = findViewById(R.id.SearchInputtxt);

        searchbtn = findViewById((R.id.Searchbtn));

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                String inputText = String.valueOf(input.getText());
                if (inputText.toLowerCase().trim().equals("glass")){
                    result.putExtra("Type", "search");
                    result.putExtra("Message", "Glass Bottle");
                    startActivity(result);
                } else if (inputText.toLowerCase().trim().equals("paper")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "paper");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("plastic")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "plastic");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("organic")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "organic");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("electronic")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "electronic");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("special")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "special");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("metal")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "metal");
                    startActivity(result);
                }else if (inputText.toLowerCase().trim().equals("battery")) {
                    result.putExtra("Type", "strategy");
                    result.putExtra("Message", "battery");
                    startActivity(result);
                }
            }
        });

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
                    startActivity(new Intent(SearchActivity.this,MapActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.miHome) {
                    startActivity(new Intent(SearchActivity.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miInfo){
                    startActivity(new Intent(SearchActivity.this,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miProfile){
                    startActivity(new Intent(SearchActivity.this,ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,CamActivity.class));
                overridePendingTransition(0,0);
            }
        });

        topAppBar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(SearchActivity.this,NewsActivity.class));
            overridePendingTransition(0,0);
        });

        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.miGame) {
                Intent game = new Intent(SearchActivity.this,GameActivity.class);
                game.putExtra("User", user);
                startActivityForResult(game, 404);
                overridePendingTransition(0,0);
                return true;
            } else {
                return false;
            }
        });
    }
    private void typeButtonIntialize(){
        plastic = findViewById(R.id.plastic);
        paper = findViewById(R.id.paper);
        glass = findViewById(R.id.glass);
        organic = findViewById(R.id.organic);
        electronic = findViewById(R.id.electronic);
        metal = findViewById(R.id.metal);
        special = findViewById(R.id.special);
        battery = findViewById(R.id.battery);

        plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "plastic");
                startActivity(result);
            }
        });
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "paper");
                startActivity(result);
            }
        });
        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "glass");
                startActivity(result);
            }
        });
        organic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "organic");
                startActivity(result);
            }
        });
        electronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "electronic");
                startActivity(result);
            }
        });
        metal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "metal");
                startActivity(result);
            }
        });
        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "special");
                startActivity(result);
            }
        });
        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(SearchActivity.this, SearchResultsActivity.class);
                result.putExtra("Type", "strategy");
                result.putExtra("Message", "battery");
                startActivity(result);
            }
        });
    }
}