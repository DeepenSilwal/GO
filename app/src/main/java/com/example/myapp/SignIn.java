package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

public class SignIn extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button SignIn;
    private int counter = 5;
    private TextView SignUp;
    private FirebaseAuth mAuth;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

         Email = (EditText)findViewById(R.id.etEmail);
         Password = (EditText)findViewById(R.id.etPassword);
         SignIn = (Button)findViewById(R.id.SignUp);
         SignUp = (TextView) findViewById(R.id.signin);
         logo = (ImageView) findViewById(R.id.imageView);

         SignIn.setOnClickListener(new View.OnClickListener() {
             @Override
                public void onClick(View v) {
                 /**
                  * validate method called here on clicking sing in button
                  */
                 validate(Email.getText().toString(), Password.getText().toString());

                }
            });

        /**
         * On clicking sign up sign up, the sign up page will be opened using following methods
         */
        SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAccount();
                }
        });

    }

    /**
     * method to validate user name and password
     */
    private void validate(String userName, String userPassword){
        if(userName.isEmpty()){
            Email.setError("Please Provide Your Email");
            Email.requestFocus();
        }
        else if(userPassword.isEmpty()){
            Password.setError("Please Provide Your Password");
            Password.requestFocus();
        }
        else if (userName.isEmpty() && userPassword.isEmpty()){
            Toast.makeText(com.example.myapp.SignIn.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
        }
        else if(!(userName.isEmpty() && userPassword.isEmpty())){
            mAuth.signInWithEmailAndPassword(userName, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(com.example.myapp.SignIn.this, "Enter Valid Email and Password", Toast.LENGTH_SHORT).show();


                            }
                            /**
                             * upon successfully sign in navigation page will be displayed using following activity
                             */
                            else{
                                startActivity(new Intent(com.example.myapp.SignIn.this, Navigation.class));
                            }
                        }
                    });
        }
        else{
            Toast.makeText(com.example.myapp.SignIn.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * starting sing up page implemented here
     */
    private void createAccount(){
            Intent intent = new Intent(com.example.myapp.SignIn.this, SignUp.class);
            startActivity(intent);
    }


}
