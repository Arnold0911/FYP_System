package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ClassifierActivity extends AppCompatActivity {
    private ClassifierFragment classifierFragment;
    private CameraFragment cameraFragment;
    BottomNavigationView bnView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int testing, testing2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);
        bnView=findViewById(R.id.navigationBar);
        /*********fragment*******/
        fragmentManager = getSupportFragmentManager();
        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                fragmentTransaction=fragmentManager.beginTransaction();
                if (id == R.id.itemInsert) {
                    classifierFragment = new ClassifierFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, classifierFragment);
                    fragmentTransaction.commit();
                    return true;
                } else if (id == R.id.itemList) {
                    cameraFragment = new CameraFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, cameraFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}