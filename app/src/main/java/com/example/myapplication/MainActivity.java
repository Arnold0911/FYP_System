package com.example.myapplication;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView weltxt;
    Intent intent;
    FirebaseAuth auth;
    DatabaseReference usersReference;
    String user;
    Button previous, next;
    ImageView event;
    int currentImg;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIInitialize();

        currentImg = 0;

        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference("user");

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        weltxt = findViewById(R.id.Welcometxt);
        lineChart = findViewById(R.id.lineChart);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        event = findViewById(R.id.event);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference eventReference = FirebaseDatabase.getInstance().getReference("event");
                if(currentImg == 0){
                    currentImg = 3;
                    eventReference.child(String.valueOf(currentImg)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String picturePath = snapshot.getValue(String.class);
                            Glide.with(MainActivity.this).load(picturePath).into(event);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Cannot get image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    currentImg--;
                    eventReference.child(String.valueOf(currentImg)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String picturePath = snapshot.getValue(String.class);
                            Glide.with(MainActivity.this).load(picturePath).into(event);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Cannot get image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference eventReference = FirebaseDatabase.getInstance().getReference("event");
                if(currentImg == 3){
                    currentImg = 0;
                    eventReference.child(String.valueOf(currentImg)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String picturePath = snapshot.getValue(String.class);
                            Glide.with(MainActivity.this).load(picturePath).into(event);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Cannot get image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    currentImg++;
                    eventReference.child(String.valueOf(currentImg)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String picturePath = snapshot.getValue(String.class);
                            Glide.with(MainActivity.this).load(picturePath).into(event);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Cannot get image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




        FirebaseUser currentUser = auth.getCurrentUser();
        String userId;
        if(currentUser != null) {
            userId = currentUser.getUid();

            usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        user = snapshot.child("name").getValue(String.class);

                        weltxt.setText("Welcome, " + user);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            weltxt.setText("Welcome, Guest");
        }
        setLineChart();
    }


    protected void setLineChart(){
        float margin = Utils.convertDpToPixel(6f);  // 6dp
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextSize(16f);
        leftAxis.setXOffset(margin);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                leftAxis.setTextColor(Color.WHITE);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                leftAxis.setTextColor(Color.BLACK);
                break;
        }

        lineChart.getDescription().setText("Recyling Garbage (per kg) / day");
        lineChart.getDescription().setTextSize(16f);
        lineChart.getDescription().setTextColor(Color.BLACK);

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setBorderColor(Color.BLACK);


        if ("".equals(user)){

            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(1, 0));
            entries.add(new Entry(2, 0));
            entries.add(new Entry(3, 0));
            entries.add(new Entry(4, 0));
            entries.add(new Entry(5, 0));
            entries.add(new Entry(6, 0));
            entries.add(new Entry(7, 0));

            LineDataSet dataSet = new LineDataSet(entries, "Recyling Garbage (per g) / day");
            dataSet.setColor(Color.BLUE);
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(true);
            dataSet.setCircleRadius(5f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setDrawValues(false);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
        }else{
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(1, 3));
            entries.add(new Entry(2, 1));
            entries.add(new Entry(3, 0));
            entries.add(new Entry(4, 0));
            entries.add(new Entry(5, 0));
            entries.add(new Entry(6, 0));
            entries.add(new Entry(7, 0));

            LineDataSet dataSet = new LineDataSet(entries, "Classifier Garbage (per g) / day");
            dataSet.setColor(Color.BLUE);
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(true);
            dataSet.setCircleRadius(5f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setDrawValues(false);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
        }
    }


    private void UIInitialize(){

        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.miHome);

        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);


        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

        bottomNavigationView.setBackground(null);

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.miMapping)
                {
                    startActivity(new Intent(MainActivity.this,MapActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.miHome) {
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miInfo){
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miProfile){
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CamActivity.class));
                overridePendingTransition(0,0);
            }
        });

        topAppBar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,NewsActivity.class));
            overridePendingTransition(0,0);
        });

        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.miGame) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                intent.putExtra("User", user);
                startActivityForResult(intent, 404);
                overridePendingTransition(0,0);
                return true;
            } else {
                return false;
            }
        });
    }
}