package com.example.pmapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class resourceDetails extends AppCompatActivity {
    Bundle intent1;
    TextView Name,Type,TimePerDay,Cost;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_details);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // curve
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-40;
        getWindow().setAttributes(params);
        getWindow().setLayout((int)(width*.8),(int)(height*.5));
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        Type=findViewById(R.id.ResourceType);
        Name=findViewById(R.id.ResourceName);
        TimePerDay=findViewById(R.id.TimePerDay);
        Cost=findViewById(R.id.Cost);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ResourceID = (String) intent1.getString("ResourceID");
            DocumentReference documentReference = db.collection("Resources").document(ResourceID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String cost = documentSnapshot.getString("Cost");
                    String resourceName = documentSnapshot.getString("ResourceName");
                    String resourceType= documentSnapshot.getString("ResourceType");
                    String timePerDay=documentSnapshot.getString("TimePerDay");
                    Cost.setText(cost);
                    Name.setText(resourceName);
                    Type.setText(resourceType);
                    TimePerDay.setText(timePerDay);
                }

            });
        }
    }

    public void backToViewResourcePage(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            Intent intent = new Intent(resourceDetails.this, ViewResources.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}