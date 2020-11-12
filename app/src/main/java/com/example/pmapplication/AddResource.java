package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddResource extends AppCompatActivity {
    Button addResource;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText ResourceName,TimePerDay,Cost;
    ProgressBar progressBar;
    RadioButton people,materials,equipment;
    String type,resourceName,timePerDay,cost,TotalCost;
    Bundle intent1;
    double Total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource);
        ResourceName=findViewById(R.id.ResourceName);
        TimePerDay=findViewById(R.id.TimePerDay);
        Cost = findViewById(R.id.Cost);
        people = findViewById(R.id.people);
        materials = findViewById(R.id.materials);
        equipment = findViewById(R.id.equipment);
        addResource = findViewById(R.id.addResource);
        progressBar=findViewById(R.id.progressBar);
        intent1 = getIntent().getExtras();
        addResource.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resourceName = ResourceName.getText().toString().trim();
                timePerDay = TimePerDay.getText().toString().trim();
                cost=Cost.getText().toString();

                if(people.isChecked())
                    type="People";
                if(materials.isChecked())
                    type="Materials";
                if(equipment.isChecked())
                    type="Equipment";
                if(TextUtils.isEmpty(resourceName)){
                    ResourceName.setError("Please Enter Resource Name, It Is Required");
                    return;
                }
                else if (TextUtils.isEmpty(timePerDay)){
                    TimePerDay.setError("Please Enter Time Per Day of Resource, It Is Required");
                    return;
                }
                else if(TextUtils.isEmpty(cost)){
                    Cost.setError("Please Enter Cost, It Is Required");
                    return;
                }
                else if(Double.parseDouble(cost)< 1){
                    Cost.setError("The Cost Must Be Positive Number");
                    return;
                }
                else if(!(TextUtils.isEmpty(resourceName) && TextUtils.isEmpty(timePerDay) && TextUtils.isEmpty(cost))) {
                    if (intent1 != null) {
                        final String ProjectID = (String) intent1.getString("ProjectID");
                        progressBar.setVisibility(View.VISIBLE);
                        addResource.setVisibility(View.GONE);
                        mAuth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();
                        Map<String, Object> Resource = new HashMap<>();
                        Resource.put("ResourceName", resourceName);
                        Resource.put("TimePerDay", timePerDay);
                        Resource.put("Cost", cost);
                        Resource.put("ProjectID", ProjectID);
                        Resource.put("ResourceType", type);
                        db.collection("Resources").add(Resource).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                DocumentReference docRef = db.collection("Projects").document(ProjectID);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document != null && document.exists()) {
                                                String Cost=document.getString("TotalCost");
                                                Total=Double.parseDouble(Cost);
                                                Double costToDouble=Double.parseDouble(cost);
                                                Double Total2=Total+costToDouble;
                                                TotalCost=Total2+"";
                                                DocumentReference documentRef = db.collection("Projects").document(ProjectID);
                                                documentRef.update("TotalCost", TotalCost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AddResource.this, "Resource Added Successfully", Toast.LENGTH_SHORT).show();
                                                        intent1 = getIntent().getExtras();
                                                        if (intent1 != null) {
                                                            final String ProjectID = (String) intent1.getString("ProjectID");
                                                            Intent intent = new Intent(AddResource.this, ViewResources.class);
                                                            Bundle b =new Bundle();
                                                            b.putString("ProjectID",ProjectID);
                                                            intent.putExtras(b);
                                                            startActivity(intent);
                                                        }
//                                                        startActivity(new Intent(AddResource.this, ViewResources.class));
                                                    }
                                                });
                                            } else {

                                            }
                                        } else {

                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddResource.this, "Something Went Wrong,Try Again ! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                addResource.setVisibility(View.VISIBLE);
                            }
                        });

                        return;
                    }
                }
            }
        });
    }

    public void backToViewResource(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            Intent intent = new Intent(AddResource.this, ViewResources.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}