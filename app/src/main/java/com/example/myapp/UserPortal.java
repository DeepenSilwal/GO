package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPortal extends AppCompatActivity {
    private TextView Email;
    private TextView FullName;
    private TextView Address;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;


    /**
     * To set the address value of user
     */
    DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portal);
        Email = (TextView)findViewById(R.id.Email);
        FullName = (TextView) findViewById(R.id.FullName);
        Address = (TextView) findViewById(R.id.Address);





        /**
         * UUID of currently signed in user
         */
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String id = currentUser.getUid();

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
                        UserObject user = new UserObject(cUser.getFullName(),cUser.getEmail(),cUser.getAddress());
                        Email.setText(user.getEmail());
                        FullName.setText(user.getFullName());
                        Address.setText(user.getAddress());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
