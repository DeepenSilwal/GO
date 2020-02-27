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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button SignIn;
    private int counter = 5;
    private TextView SignUp;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

         Email = (EditText)findViewById(R.id.etEmail);
         Password = (EditText)findViewById(R.id.etPassword);
         SignIn = (Button)findViewById(R.id.SignUp);
         SignUp = (TextView) findViewById(R.id.signin);

         SignIn.setOnClickListener(new View.OnClickListener() {
             @Override
                public void onClick(View v) {
                    validate(Email.getText().toString(), Password.getText().toString());

                }
            });
         SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAccount();

                }
        });

    }


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
            Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
        }
        else if(!(userName.isEmpty() && userPassword.isEmpty())){
            mAuth.signInWithEmailAndPassword(userName, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                            }
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void createAccount(){
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
    }


}
