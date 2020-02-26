package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
//import android.support.v4.app.FragmentActivity;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class  SecondActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ClaimAddress;
    private EditText StartLocation;
    private EditText EndLocation;
    private ImageButton profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        profilebutton = (ImageButton)findViewById(R.id.imageButton);
        mMap = googleMap;
        // Add a marker in Kathmandu, Nepal, and move the camera.
        LatLng Nepal = new LatLng(27.605064, 85.363617);
        mMap.addMarker(new MarkerOptions().position(Nepal).title("Marker in Kathmandu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nepal));
        mMap.setMinZoomPreference(14);
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);

        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
            }
        });
    }
}
