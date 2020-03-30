package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ClaimAddress;
    private EditText StartLocation;
    private EditText EndLocation;
    private Button Start;
    private ImageButton profilebutton;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String standardAddress;

    private Polyline currentPolyline;



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

        /**
         * Adding Navigation information such as start location, end location and start button
         */
        StartLocation =(EditText)findViewById(R.id.editText2);
        EndLocation = (EditText)findViewById(R.id.editText3);
        Start = (Button)findViewById(R.id.startbutton);


        mMap = googleMap;
        // Add a marker in Kathmandu, Nepal, and move the camera.
        LatLng Nepal = new LatLng(27.605064, 85.363617);
        mMap.addMarker(new MarkerOptions().position(Nepal).title("Random Location").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nepal));
        mMap.setMinZoomPreference(14);
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);


        /**
         * Dragging the marker displays the latitude and longitude location
         */
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            /**
             * Method excuted when the user drags the marker and stops at the end position
             * @param marker
             */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng toposition = marker.getPosition();

                /**
                 * Get the latitude and longitude form the marker
                 */
                final Double lat = toposition.latitude;
                final Double lon = toposition.longitude;


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

                            /**
                             * All the variables below is the value of latitude and longitude matrix read from firebase database
                             */
                            fireLeftLong = address.left_log;
                            fireRightLong = address.right_log;
                            fireTopLat = address.top_lat;
                            fireBottomLat = address.bottom_lat;

                            /**
                             * Condition to check whether the latitude and longitude retrieved upon dragging the marker falls on specific grid
                             * Address is displayed of the particular grid when user drags and points to that grid
                             */
                            if(lat <= fireTopLat && lat > fireBottomLat && lon <= fireRightLong && lon > fireLeftLong ){
                                Toast.makeText(getApplicationContext(),address.Standard_address,Toast.LENGTH_LONG).show();

                                /**
                                 * When the marker is set; retrieves the standard address from address object
                                 */
                                standardAddress = address.Standard_address;
                            }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

        /**
         * Claim address implemented in this function
         */
        ClaimAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Get the user id of currently signed in user
                 */
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String id = currentUser.getUid();
                //Toast.makeText(Navigation.this,id,Toast.LENGTH_LONG).show();

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
                                 * Push the value to the database of currently signedin user
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
            }
        });

        /**
         * Navigation Implemented in this function
         */
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String start_address = StartLocation.getText().toString();
                final String end_address = EndLocation.getText().toString();


                if(start_address.isEmpty() || end_address.isEmpty()){
                    if(start_address.isEmpty()){
                        StartLocation.setError("Please Provide Start Address");
                    }
                    else if (end_address.isEmpty()){
                        EndLocation.setError("Please Provide End Address");
                    }
                }
                else{
                    mDatabase = FirebaseDatabase.getInstance();
                    mRef = mDatabase.getReference();
                    mRef.child("address").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Double startlatitude = null;
                            Double startlongitude = null;
                            Double endlatitude = null;
                            Double endlongitude = null;

                            for(DataSnapshot addressObj: dataSnapshot.getChildren()){
                                Address address = addressObj.getValue(Address.class);
                                if(start_address.equals(address.Standard_address)){
                                    startlatitude = address.top_lat;
                                    startlongitude = address.right_log;
                                }
                                if(end_address.equals(address.Standard_address)){
                                    endlatitude = address.top_lat;
                                    endlongitude = address.right_log;
                                }
                            }

                            if(startlatitude != null && endlatitude != null  && startlongitude != null && endlongitude != null){
                                /**
                                 * Adding Marker to start and end location
                                 */
                                final MarkerOptions place1 = new MarkerOptions().position(new LatLng(startlatitude,startlongitude)).title("start Location");
                                final MarkerOptions place2 = new MarkerOptions().position(new LatLng(endlatitude,endlongitude)).title("End Location");
                                mMap.addMarker(place1);
                                mMap.addMarker(place2);


                                Object[] dataTransfer = new Object[4];

                                /**
                                 * Convert Double to string
                                 */
                                String stringStartLatitude = Double.toString(startlatitude);
                                String stringStartLongitude = Double.toString(startlongitude);
                                String stringEndLatitude = Double.toString(endlatitude);
                                String stringEndLongitude = Double.toString(endlongitude);

                                String origin = "origin=" + stringStartLatitude + "," + stringStartLongitude;
                                String destination = "&destination=" + stringEndLatitude + "," + stringEndLongitude;
                                /**
                                 * Url to fetch the data from google-maps-api
                                 */
                                String url = "https://maps.googleapis.com/maps/api/directions/json?" + origin + destination +"&mode=driving" + "&key=AIzaSyBGEvQ8nUKZJRr5D_cjvfRFXx7j86vPuGY";

                                /**
                                 * Creating object of GetDirectionClass
                                 */
                                GetDirectionsData getDirectionsData = new GetDirectionsData(getApplicationContext());
                                dataTransfer[0] = mMap;
                                dataTransfer[1] = url;
                                dataTransfer[2] = new LatLng(startlatitude,startlongitude);
                                dataTransfer[3] = new LatLng(endlatitude,endlongitude);

                                getDirectionsData.execute(dataTransfer);

                            }
                            else{
                                Toast.makeText(Navigation.this,"Address Not Found",Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

    }


}
