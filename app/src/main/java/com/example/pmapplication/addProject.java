package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.DatePickerDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class addProject extends AppCompatActivity {
    Button addProject;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatePickerDialog datepicker;
    EditText projectBudget,projectName,errorStartDate,errorEndtDate;
    TextView dateOfPickUp,dateOfPickUp2;
    Calendar calendar;
    int day,month,year;
    String Name,budget,startTime,finishTime,managerId;
    final SimpleDateFormat curFormater = new SimpleDateFormat("dd/mm/yyyy");
    Date CurrentDateObj = new Date();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        projectName=findViewById(R.id.projectName);
        projectBudget=findViewById(R.id.budget);
        dateOfPickUp = findViewById(R.id.startDate);
        dateOfPickUp2 = findViewById(R.id.endDate);
        addProject=findViewById(R.id.addProject);
        errorStartDate=findViewById(R.id.errorStartDate);
        errorEndtDate=findViewById(R.id.errorEndDate);
        progressBar=findViewById(R.id.progressBar);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateOfPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker = new DatePickerDialog(addProject.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateOfPickUp.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        dateOfPickUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker = new DatePickerDialog(addProject.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateOfPickUp2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        addProject.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Name = projectName.getText().toString().trim();
                budget = projectBudget.getText().toString().trim();
                startTime=dateOfPickUp.getText().toString();
                finishTime=dateOfPickUp2.getText().toString();
                boolean isValidStartTime=true;
                boolean isValidFinishTime=true;
                String currentDate=day+"/"+month+"/"+year;

                if(TextUtils.isEmpty(Name)){
                    projectName.setError("Please Enter Project Name, It Is Required");
                    return;
                }
                 else if (TextUtils.isEmpty(startTime)){
                    errorStartDate.setError("Please Enter Start Date, It Is Required");
                    return;
                }
                else if (TextUtils.isEmpty(finishTime)){
                    errorEndtDate.setError("Please Enter Finish Date, It Is Required");
                    return;
                }
                else if(TextUtils.isEmpty(budget)){
                    projectBudget.setError("Please Enter budget, It Is Required");
                    return;
                }
                else if(Double.parseDouble(budget)< 1){
                    projectBudget.setError("The budget Must Be Positive Number");
                    return;
                }
                else if(!(TextUtils.isEmpty(Name) && TextUtils.isEmpty(budget) && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(finishTime))){
                    try {
                        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(currentDate);
                        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(startTime);
                        if(date1.compareTo(date2) > 0) {
                            errorStartDate.setError("Please Enter a valid date, not past");
                            isValidStartTime=false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(startTime);
                        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(finishTime);
                        if(date1.compareTo(date2) > 0 || date1.compareTo(date2) == 0) {
                            errorEndtDate.setError("Please Enter a valid date, not before or equal Start Date");
                            isValidFinishTime=false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(isValidStartTime && isValidFinishTime){
                        progressBar.setVisibility(View.VISIBLE);
                        addProject.setVisibility(View.GONE);
                        mAuth = FirebaseAuth.getInstance();
                        managerId = mAuth.getCurrentUser().getUid();
                        db = FirebaseFirestore.getInstance();
                        Map<String,Object> Project = new HashMap<>();
                        Project.put("ProjectName",Name);
                        Project.put("StartDate",startTime);
                        Project.put("FinishDate",finishTime);
                        Project.put("Budget",budget);
                        Project.put("managerID",managerId);
                        db.collection("Projects").add(Project).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(addProject.this, "Project Added Successfully" , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(addProject.this,  HomePage.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addProject.this, "Something Went Wrong,Try Again ! " , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                addProject.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    return;
                }
            }
        });
    }
    public void backToHomePage(View view) {
        startActivity(new Intent(addProject.this,HomePage.class));
    }

}