package com.example.pmapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(HomePage.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomePage.this,  Login.class));
    }

    public void OpenAddProject(View view) {
        startActivity(new Intent(HomePage.this,  addProject.class));
    }
}