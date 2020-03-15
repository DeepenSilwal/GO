package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText FullName;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;
    private Button SignUp;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private TextView SignIn;

    /**
     * databaseUser to add the user to firebase database
     */
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        FullName = (EditText)findViewById(R.id.fullname);
        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        ConfirmPassword = (EditText)findViewById(R.id.ConfirmPassword);
        SignUp = (Button)findViewById(R.id.SignUp);
        SignIn = (TextView)findViewById(R.id.signin);

        databaseUser = FirebaseDatabase.getInstance().getReference("UserObject");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = Email.getText().toString();
                String paswd = Password.getText().toString();
                String fullname = FullName.getText().toString();
                String confpaswd = ConfirmPassword.getText().toString();

                /**
                 * if-else conditions to check if all the fields are filled
                 */
                if(emailID.isEmpty()){
                    Email.setError("Please Provide Your Email");
                    Email.requestFocus();
                }
                else if(paswd.isEmpty()){
                    Password.setError("Please Provide Your Password");
                    Password.requestFocus();
                }
                else if(fullname.isEmpty()){
                    FullName.setError(("Please Provide Your Full Name"));
                    FullName.requestFocus();
                }
                else if(confpaswd.isEmpty()){
                    ConfirmPassword.setError("Please Confirm Your Password");
                    ConfirmPassword.requestFocus();
                }
                else if(!(paswd.equals(confpaswd))){
                    Toast.makeText(SignUp.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                }
                /**
                 * if all the fields are filled then the following method is implemented
                 */
                else if (!(emailID.isEmpty() && paswd.isEmpty() && fullname.isEmpty() && confpaswd.isEmpty())){
                    /**
                     * Authentication email and password is saved for authentication and in user object
                     */
                    mAuth.createUserWithEmailAndPassword(emailID,paswd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (!task.isSuccessful()){
                               Toast.makeText(SignUp.this.getApplicationContext(), "SignUp Unsuccessfull: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                           else{

                               Log.d(TAG, "createUserWithEmail:success");

                               /**
                                * add user method called here
                                */
                               addUser();

                               startActivity(new Intent(SignUp.this, com.example.myapp.SignIn.class));
                           }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });

        /**
         * On Click to the sing in button sign in page will be displayed
         */
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (SignUp.this, com.example.myapp.SignIn.class));
            }
        });

    }

    /**
     * Method to add user to database implemented
     */
    private void addUser(){
        String emailID = Email.getText().toString().trim();
        String fullname = FullName.getText().toString();

        /**
         * Get the user id generated in authentication
         */
        FirebaseUser userid = mAuth.getCurrentUser();
        String id = userid.getUid();

        /**
         * User object is saved in database inside userobject
         */
        UserObject user = new UserObject(fullname, emailID);
        databaseUser.child(id).setValue(user);


    }
    
}
