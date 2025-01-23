package com.example.myapplication;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class GameActivity extends AppCompatActivity {

    ImageButton webGame, MiniGame;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        webGame = findViewById(R.id.webGame);
        MiniGame = findViewById(R.id.javaGame);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.setting) {
                    startActivity(new Intent(GameActivity.this,ProfileActivity.class));
                    return true;
                } else if (id == R.id.Scan) {
                    Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                    return true;
                } else if (id == R.id.buttonLogout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(GameActivity.this,LoginActivity.class));
                    return true;
                }
                return false;
            }
        });

        webGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webGameIntent = new Intent(GameActivity.this, WebGameActivity.class);
                startActivity(webGameIntent);
            }
        });

        MiniGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webGameIntent = new Intent(GameActivity.this, MiniGameActivity.class);
                startActivity(webGameIntent);
            }
        });
    }

}