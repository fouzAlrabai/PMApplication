package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity{
    EditText mUsername, mEmail, mPassword;
    Button register;
   FirebaseAuth mAuth;
   FirebaseFirestore db;
    String username,email;
    String userId;
    public static final String TAG = "Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsername = findViewById(R.id.name);
        mEmail = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        register = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username= mUsername.getText().toString();
                email = mEmail.getText().toString().trim();

                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Please Enter Your Name, It is Required");
                    return;

                }if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please Enter Your Email, It Is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please Enter Your Password, It Is Required");
                    return;
                }

                if (password.length() < 8) {
                    mPassword.setError("The Characters Must Be At Least 8 Characters ");
                    return;
                }
                //register user
               mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // تشيك للايميل اذا صدقي او لا
                            FirebaseUser fuser = mAuth.getCurrentUser();
                           fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email Has Been Sent ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"OnFailure: Email Not Sent");
                                }
                            });
//// NEED TO WAIT FOR Abby
                            userId = mAuth.getCurrentUser().getUid();
                             Toast.makeText(Register.this, "Registration Was Successful!!", Toast.LENGTH_SHORT).show();

                            // Donator don = new Donator(username,email,gender.getText().toString());
                            db= FirebaseFirestore.getInstance();
                            final DocumentReference documentReference=db.collection("Users").document(userId);
//
//
//                            String token_id= FirebaseInstanceId.getInstance().getToken();
                           Map<String,Object> users = new HashMap<>();
                            users.put("UserName",username);
                            users.put("Email",email);
//                            donators.put("Gender",gen);
//                            donators.put("PhoneNumber","05xxxxxxxx");
//                            donators.put("token_id", token_id);
                          documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                     Log.d(TAG,"OnSuccess: user profile is created for"+userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                     Log.d(TAG,"OnFailure "+ e.toString());
                                }
                            });





                            //db.collection("users").document(userid).set(don);

                           /* db = FirebaseFirestore.getInstance();
                            String  USER = mAuth.getCurrentUser().getUid();
                            Donator donator = new Donator(username,email,(String)gender.getText());
                            db.collection("users").document(USER).set(donator);*/


                            startActivity(new Intent(Register.this, HomePage.class));
                        } else {
                            Toast.makeText(Register.this, "Something Went Wrong,Try Again !  " , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void OpenLoginPage(View view) {
        startActivity(new Intent(Register.this,Login.class));
    }
}
