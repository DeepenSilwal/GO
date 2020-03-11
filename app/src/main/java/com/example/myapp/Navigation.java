package com.example.myapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.support.v4.app.FragmentActivity;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ClaimAddress;
    private EditText StartLocation;
    private EditText EndLocation;
    private ImageButton profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * Adding profile button to navigation page
         */
        profilebutton = (ImageButton)findViewById(R.id.imageButton);

        mMap = googleMap;
        // Add a marker in Kathmandu, Nepal, and move the camera.
        LatLng Nepal = new LatLng(27.605064, 85.363617);
        mMap.addMarker(new MarkerOptions().position(Nepal).title("Marker in Kathmandu").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nepal));
        mMap.setMinZoomPreference(14);
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);


        /**
         * Dragging the marker displays the latitude and longitude location
         */
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                LatLng fromposition = marker.getPosition();
                //Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f", fromposition.latitude,fromposition.longitude));
                //Toast.makeText(Navigation.this,String.format("Drag from %f:%f",fromposition.latitude,fromposition.longitude),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng toposition = marker.getPosition();
                //Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f", position.latitude,position.longitude));
                //Toast.makeText(Navigation.this,String.format("Drag from %f:%f",position.latitude,position.longitude),Toast.LENGTH_LONG).show();
                /**
                 * shows the snippet of latitude and logitude
                 */
                Toast.makeText(getApplicationContext(),"Marker " + toposition,Toast.LENGTH_LONG).show();


                /**
                 * shows the snippet of address

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses;
                try{
                    addresses = geocoder.getFromLocation(toposition.latitude, toposition.longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();

                    Toast.makeText(Navigation.this,"Address: " + address + " " + city,Toast.LENGTH_LONG).show();
                }catch(IOException e){
                    e.printStackTrace();
                }*/
            }
        });

        /**
         * On click to the profile button profile page will be displayed
         */
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Navigation.this, UserPortal.class));
            }
        });

    }
}
