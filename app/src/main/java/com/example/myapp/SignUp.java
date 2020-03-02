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
                else if (!(emailID.isEmpty() && paswd.isEmpty() && fullname.isEmpty() && confpaswd.isEmpty())){

                    mAuth.createUserWithEmailAndPassword(emailID,paswd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (!task.isSuccessful()){
                               Toast.makeText(SignUp.this.getApplicationContext(), "SignUp Unsuccessfull: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                           else{
                               addUser();
                               Log.d(TAG, "createUserWithEmail:success");
                               FirebaseUser user = mAuth.getCurrentUser();
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
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (SignUp.this, com.example.myapp.SignIn.class));
            }
        });

    }

    private void addUser(){
        String emailID = Email.getText().toString().trim();
        String fullname = FullName.getText().toString();
        String paswd = Password.getText().toString();
        String confpaswd = ConfirmPassword.getText().toString();

        String id = databaseUser.push().getKey();//create a unique id
        UserObject user = new UserObject(fullname, emailID);
        databaseUser.child(id).setValue(user);


    }
    
}
