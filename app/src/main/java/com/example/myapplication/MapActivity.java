package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataType.news;
import com.example.myapplication.adapter.newsAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private PlacesClient placesClient;
    private Location currentLocation;
    FirebaseAuth auth;
    DatabaseReference recyclingReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        Places.initialize(getApplicationContext(), "AIzaSyANyiZfVELnnaOWLxVnqXbfPIBuWOCgeUo");
        placesClient = Places.createClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        auth = FirebaseAuth.getInstance();
        recyclingReference = FirebaseDatabase.getInstance().getReference("recycling");

        UIInitialize();

        fetchLocation();
//        geoLocate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fetchRecyclingStations();
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            fetchLocation();
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLocation = location;
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
                        }
                    });
        }
    }

//    private void geoLocate() {
//        String searchString = "Hospital";
//
//        Geocoder geocoder = new Geocoder(MapActivity.this);
//        List<Address> list = new ArrayList<>();
//        try {
//            list = geocoder.getFromLocationName(searchString, 1);
//        } catch (IOException e) {
//            Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
//        }
//
//        if (list.size() > 0) {
//            Address address = list.get(0);
//            Log.d(TAG, "geolocate: found a location: " + address.toString());
//            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title("Recycling Station"));
//            mMap.moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
//        }
//    }

    private void fetchRecyclingStations() {
        recyclingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String newsId = snapshot.getKey();

                    recyclingReference.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("location")) {
                                String location = snapshot.child("location").getValue(String.class);
                                if(location != null) {
                                    String locationName = snapshot.child("name").getValue(String.class);
                                    String[] loc = location.split(",");

                                    LatLng latLng = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
//                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                } else {
                                    Toast.makeText(MapActivity.this, "Recycling Station nearby not defined", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MapActivity.this, "location getting error", Toast.LENGTH_SHORT).show();
                            }


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

    private void UIInitialize(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        BottomNavigationView bottomNavigationView
                = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.miMapping);

        MaterialToolbar topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);

        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

        bottomNavigationView.setBackground(null);

        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.miMapping) {
                    return true;
                } else if (item.getItemId() == R.id.miHome) {
                    startActivity(new Intent(MapActivity.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miInfo){
                    startActivity(new Intent(MapActivity.this,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(item.getItemId() == R.id.miProfile){
                    startActivity(new Intent(MapActivity.this,ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        topAppBar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(MapActivity.this,NewsActivity.class));
            overridePendingTransition(0,0);
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this,CamActivity.class));
                overridePendingTransition(0,0);
            }
        });

        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.miGame) {
                Intent game = new Intent(MapActivity.this,GameActivity.class);

                startActivityForResult(game, 404);
                overridePendingTransition(0,0);
                return true;
            } else {
                return false;
            }
        });
    }
}