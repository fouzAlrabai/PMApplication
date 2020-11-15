package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.R.layout;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.channels.UnsupportedAddressTypeException;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_add_task extends AppCompatActivity {
    Button addTask;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText TaskName,errorStartDate,errorEndtDate,TsakID;
    DatePickerDialog datepicker;
    Calendar calendar;
    int day,month,year;
    TextView dateOfPickUp,dateOfPickUp2;
    ProgressBar progressBar;
    String TaskNameS,startTime,finishTime,TaskIDS;
    Spinner spinerP,spinnerE,spinnerM;
    ArrayList <String> Eq;
    Bundle intent1;
    LinearLayout checkBoxs,Materials,Equipment;
    Resource resource;
    ArrayList<Resource> resources,resources2,resources3,selected;
    ArrayList<String> selectedresource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Eq = new ArrayList<String>();
//        spinerP=findViewById(R.id.Spinner_p);
//        spinnerE=findViewById(R.id.Spinner_e);
//        spinnerM=findViewById(R.id.Spinner_m);
        TaskName = findViewById(R.id.TaskName);
        TsakID = findViewById(R.id.TaskId);
        addTask = findViewById(R.id.addTask);
        progressBar = findViewById(R.id.progressBar);
        dateOfPickUp = findViewById(R.id.startDateT);
        dateOfPickUp2 = findViewById(R.id.endDateT);
        errorStartDate = findViewById(R.id.errorStartDate);
        errorEndtDate = findViewById(R.id.errorEndDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        intent1 = getIntent().getExtras();
        checkBoxs = findViewById(R.id.checkbox);
        Materials = findViewById(R.id.Materials);
        Equipment = findViewById(R.id.Equipment);
        db = FirebaseFirestore.getInstance();
        resources = new ArrayList<Resource>();
        resources2 = new ArrayList<Resource>();
        resources3 = new ArrayList<Resource>();
        selected = new ArrayList<Resource>();
        selectedresource = new ArrayList<String>();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            db.collection("Resources")
                    .whereEqualTo("ResourceType", "People").whereEqualTo("ProjectID", ProjectID)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("", "listen:error", e);
                                return;
                            }
                            for (QueryDocumentSnapshot document : snapshots) {
                                String Cost = document.getString("Cost");
                                String projectID = document.getString("ProjectID");
                                String ResourceName = document.getString("ResourceName");
                                String ResourceType = document.getString("ResourceType");
                                String TimePerDay = document.getString("TimePerDay");
                                resource = new Resource(document.getId(), Cost, projectID, ResourceName, ResourceType, TimePerDay);
                                resources.add(resource);
                            }

                            for (int i = 0; i < resources.size(); i++) {
                                CheckBox ch = new CheckBox(getApplicationContext());
                                ch.setTextColor(Color.BLACK);
                                ch.setText(resources.get(i).ResourceName);
                                checkBoxs.addView(ch);

                                ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            selectedresource.add(buttonView.getText().toString());
                                        } else {
                                            selectedresource.remove(buttonView.getText().toString());
                                        }
                                    }
                                });
                            }

                        }// end if null
                    });
        db.collection("Resources")
                .whereEqualTo("ResourceType", "Materials").whereEqualTo("ProjectID", ProjectID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : snapshots) {
                            String Cost = document.getString("Cost");
                            String projectID = document.getString("ProjectID");
                            String ResourceName = document.getString("ResourceName");
                            String ResourceType = document.getString("ResourceType");
                            String TimePerDay = document.getString("TimePerDay");
                            resource = new Resource(document.getId(), Cost, projectID, ResourceName, ResourceType, TimePerDay);
                            resources2.add(resource);
                        }

                        for (int i = 0; i < resources2.size(); i++) {
                            CheckBox ch = new CheckBox(getApplicationContext());
                            ch.setTextColor(Color.BLACK);
                            ch.setText(resources2.get(i).ResourceName);
                            Materials.addView(ch);

                            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        selectedresource.add(buttonView.getText().toString());
                                    } else {
                                        selectedresource.remove(buttonView.getText().toString());
                                    }
                                }
                            });
                        }

                    }// end if null
                });

        db.collection("Resources")
                .whereEqualTo("ResourceType", "Equipment").whereEqualTo("ProjectID", ProjectID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : snapshots) {
                            String Cost = document.getString("Cost");
                            String projectID = document.getString("ProjectID");
                            String ResourceName = document.getString("ResourceName");
                            String ResourceType = document.getString("ResourceType");
                            String TimePerDay = document.getString("TimePerDay");
                            resource = new Resource(document.getId(), Cost, projectID, ResourceName, ResourceType, TimePerDay);
                            resources3.add(resource);
                        }
                        int j = 0;
                        for (int i = 0; i < resources3.size(); i++) {
                            CheckBox ch = new CheckBox(getApplicationContext());
                            ch.setTextColor(Color.BLACK);
                            ch.setText(resources3.get(i).ResourceName);
                            Equipment.addView(ch);

                            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        selectedresource.add(buttonView.getText().toString());
                                    } else {
                                        selectedresource.remove(buttonView.getText().toString());

                                    }
                                }
                            });
                            j++;
                        }

                    }// end if null
                });
    }
        dateOfPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker = new DatePickerDialog(activity_add_task.this,
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
                datepicker = new DatePickerDialog(activity_add_task.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateOfPickUp2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                double cost=0;
                TaskNameS = TaskName.getText().toString().trim();
                TaskIDS= TsakID.getText().toString().trim();
                startTime=dateOfPickUp.getText().toString();
                finishTime=dateOfPickUp2.getText().toString();
                boolean isValidStartTime=true;
                boolean isValidFinishTime=true;
                String currentDate=day+"/"+month+"/"+year;


                if(TextUtils.isEmpty(TaskIDS)){
                    TsakID.setError("Please Enter Task ID, It Is Required");
                    return;
                }
                else if(TextUtils.isEmpty(TaskNameS)){
                    TaskName.setError("Please Enter Task Name, It Is Required");
                    return;
                } else if (TextUtils.isEmpty(startTime)){
                    errorStartDate.setError("Please Enter Start Date, It Is Required");
                    return;
                } else if (TextUtils.isEmpty(finishTime)){
                    errorEndtDate.setError("Please Enter Finish Date, It Is Required");
                    return;
                }else
                    if(!(TextUtils.isEmpty(TaskNameS) && TextUtils.isEmpty(startTime) && TextUtils.isEmpty(finishTime))){
                    try {
                        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(currentDate);
                        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(startTime);
                        if(date1.compareTo(date2) > 0) {
                            errorStartDate.setError("Please Enter a Valid Date, Not Past");
                            isValidStartTime=false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(startTime);
                        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(finishTime);
                        if(date1.compareTo(date2) > 0 || date1.compareTo(date2) == 0) {
                            errorEndtDate.setError("Please Enter a Valid Date, Not Before or Equal Start Date");
                            isValidFinishTime=false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        for (int i=0;i<resources.size();i++){
                            for(int j=0;j<selectedresource.size();j++){
                                if(resources.get(i).ResourceName==selectedresource.get(j)){
                                    selected.add(resources.get(i));
                                }
                            }
                        }

                        for (int i=0;i<resources2.size();i++){
                            for(int j=0;j<selectedresource.size();j++){
                                if(resources2.get(i).ResourceName==selectedresource.get(j)){
                                    selected.add(resources2.get(i));
                                }
                            }
                        }

                        for (int i=0;i<resources3.size();i++){
                            for(int j=0;j<selectedresource.size();j++){
                                if(resources3.get(i).ResourceName==selectedresource.get(j)){
                                    selected.add(resources3.get(i));
                                }
                            }
                        }

                        for (int i=0;i<selected.size();i++){
                            cost=cost+Double.parseDouble(selected.get(i).Cost);
                        }


          if(isValidStartTime && isValidFinishTime)  {
                    if (intent1 != null) {
                        final String ProjectID = (String) intent1.getString("ProjectID");
                        progressBar.setVisibility(View.VISIBLE);
                        addTask.setVisibility(View.GONE);
                        mAuth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();
                        Map<String, Object> Task = new HashMap<>();
                        Task.put("TaskName", TaskNameS);
                        Task.put("TaskID", TaskIDS);
                        Task.put("ProjectID", ProjectID);
                        Task.put("StartDate", startTime);
                        Task.put("FinishDate", finishTime);
                        Task.put("TaskCost", cost);
                        db.collection("Task").add(Task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(activity_add_task.this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                                intent1 = getIntent().getExtras();
                                if (intent1 != null) {
                                    for (int i=0;i<selected.size();i++){
                                        String resourceName=selected.get(i).ResourceName;
                                        String timePerDay=selected.get(i).TimePerDay;
                                        String C=selected.get(i).Cost;
                                        String ProjectID=selected.get(i).ProjectID;
                                        String type=selected.get(i).ResourceType;
                                        Map<String, Object> Resource = new HashMap<>();
                                        Resource.put("ResourceName", resourceName);
                                        Resource.put("TimePerDay", timePerDay);
                                        Resource.put("Cost", C);
                                        Resource.put("ProjectID", ProjectID);
                                        Resource.put("ResourceType", type);
                                        db.collection("Task").document(documentReference.getId()).collection("Resources").add(Resource).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                DocumentReference docRef = db.collection("Task").document(ProjectID);
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                        } else {

                                                        }
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(activity_add_task.this, "Something Went Wrong,Try Again ! ", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                    final String ProjectID = (String) intent1.getString("ProjectID");
                                    Intent intent = new Intent(activity_add_task.this, task_list.class);
                                    Bundle b = new Bundle();
                                    b.putString("ProjectID", ProjectID);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
//                                                        startActivity(new Intent(AddResource.this, ViewResources.class));
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_add_task.this, "Something Went Wrong,Try Again ! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                addTask.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    }
                        return;

                }
            }
        });
    }


    public void backTaskList(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            Intent intent = new Intent(activity_add_task.this, task_list.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}

