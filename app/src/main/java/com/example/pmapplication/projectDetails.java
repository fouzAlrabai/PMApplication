package com.example.pmapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class projectDetails extends AppCompatActivity {
    TextView ProjectName,StartDate,FinishDate,Budget,TotalCost;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Bundle intent1;
    String managerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        ProjectName=findViewById(R.id.ProjectName);
        StartDate=findViewById(R.id.StartDate);
        FinishDate=findViewById(R.id.FinishDate);
        Budget=findViewById(R.id.Budget);
        TotalCost=findViewById(R.id.TotalCost);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        managerID =mAuth.getCurrentUser().getUid();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            DocumentReference documentReference = db.collection("Projects").document(ProjectID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String projectName = documentSnapshot.getString("ProjectName");
                    String startDate = documentSnapshot.getString("StartDate");
                    String finishDate= documentSnapshot.getString("FinishDate");
                    String budget=documentSnapshot.getString("Budget");
                    String totalCost=documentSnapshot.getString("TotalCost");
                    ProjectName.setText(projectName);
                    StartDate.setText(startDate);
                    FinishDate.setText(finishDate);
                    Budget.setText(budget);
                    TotalCost.setText(totalCost);
                }

            });
        }
    }

    public void backToHomePage(View view) {
        startActivity(new Intent(projectDetails.this,HomePage.class));

    }
}