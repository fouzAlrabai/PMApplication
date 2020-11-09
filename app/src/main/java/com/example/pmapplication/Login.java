package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity{
    EditText userEmail,userPassword;
    Button loginbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userId;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loginbtn = findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar2);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Please Enter Your Email, It Is Required");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    userPassword.setError("Please Enter Password, It Is Required");
                    return;
                }
                else if(password.length() < 8){
                    userPassword.setError("The Characters Must Be At Least 8 Characters");
                    return;
                }
                else if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    userEmail.setError("Please Enter Your Email and Password, It Is Required");
                    return;
                }
                else if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginbtn.setVisibility(View.GONE);
                    // authenticate the user
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,  HomePage.class));
                            }else {
                                Toast.makeText(Login.this, "Something Went Wrong,Try Again ! " , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                loginbtn.setVisibility(View.VISIBLE);
                            }

                        }
                    });


                }

            }
        });

    }
    public void OpenRegisterPage(View view) {
        startActivity(new Intent(Login.this,Register.class));
    }
}
