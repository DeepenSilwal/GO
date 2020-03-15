package com.example.myapp;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.support.v4.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ClaimAddress;
    private EditText StartLocation;
    private EditText EndLocation;
    private ImageButton profilebutton;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String standardAddress;


    /**
     * To set the address value of user
     */
    DatabaseReference databaseUser;

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
        ClaimAddress = (Button)findViewById(R.id.button2);



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
                //Toast.makeText(getApplicationContext(),"Marker " + toposition,Toast.LENGTH_LONG).show();

                /**
                 * Get the latitude and longitude form the marker
                 */
                final Double lat = toposition.latitude;
                final Double lon = toposition.longitude;

                //Toast.makeText(getApplicationContext(),"Marker " + lat + " , "+ lon,Toast.LENGTH_LONG).show();

                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference();
                mRef.child("address").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Double fireLeftLong;
                        Double fireRightLong;
                        Double fireTopLat;
                        Double fireBottomLat;

                        /**
                         * Reading data from address dataset located in firebase
                         * Each obj represents the object of the address
                         */
                        for(DataSnapshot obj: dataSnapshot.getChildren()){

                            /**
                             * This Address class represented is used to retrieve data as objects
                             */
                            Address address = obj.getValue(Address.class);
                            //Toast.makeText(getApplicationContext(),address.left_log + address.right_log + address.top_lat + address.bottom_lat,Toast.LENGTH_LONG).show();

                            /**
                             * All the variables below is the value of latitude and longitude matrix read from firebase database
                             */
                            fireLeftLong = Double.valueOf(address.left_log);
                            fireRightLong = Double.valueOf(address.right_log);
                            fireTopLat = Double.valueOf(address.top_lat);
                            fireBottomLat = Double.valueOf(address.bottom_lat);

                            /**
                             * Condition to check whether the latitude and longitude retrieved upon dragging the marker falls on specific grid
                             * Address is displayed of the particular grid when user drags and points to that grid
                             */
                            if(lat <= fireTopLat && lat > fireBottomLat && lon <= fireRightLong && lon > fireLeftLong ){
                                Toast.makeText(getApplicationContext(),address.standard_address,Toast.LENGTH_LONG).show();

                                /**
                                 * When the marker is set; retrieves the standard address from address object
                                 */
                                standardAddress = address.standard_address;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



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


        ClaimAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Get the user id of currently signed in user
                 */
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String id = currentUser.getUid();
                //Toast.makeText(Navigation.this,id,Toast.LENGTH_LONG).show();

                /**
                 *
                 */
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference();
                mRef.child("UserObject").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot obj: dataSnapshot.getChildren()){
                            /**
                             * get the user object children information from database UserObject
                             */
                            UserObject cUser = obj.getValue(UserObject.class);

                            if(id.equals(obj.getKey())){

                                /**
                                 * Get the reference of UserObject
                                 */
                                databaseUser = FirebaseDatabase.getInstance().getReference("UserObject");

                                /**
                                 * Setting the UserObject with existing username, email and adding with new address
                                 */
                                UserObject user = new UserObject(cUser.getFullName(),cUser.getEmail(),standardAddress);

                                /**
                                 * Push the value to the database of currently signed in user
                                 */
                                databaseUser.child(id).setValue(user);
                            }
                           // Toast.makeText(Navigation.this,obj.getKey(),Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //UserObject user = new UserObject(standardAddress);

                //databaseUser.child(databaseUser.getKey()).setValue(user);
            }
        });

    }


}
