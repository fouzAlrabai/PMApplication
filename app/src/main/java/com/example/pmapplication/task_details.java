package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class task_details extends AppCompatActivity {
    Bundle intent1;
    TextView taskID,taskName,startDate,finishDate,taskCost;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LinearLayout member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // curve
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=100;
        params.y=400;
        getWindow().setAttributes(params);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        taskID=findViewById(R.id.taskId);
        taskName=findViewById(R.id.taskName);
        startDate=findViewById(R.id.startDate);
        finishDate=findViewById(R.id.finishDate);
        taskCost=findViewById(R.id.taskCost);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        member=findViewById(R.id.member);
        if (intent1 != null) {
            final String TaskID = (String) intent1.getString("TaskID");
            DocumentReference documentReference = db.collection("Task").document(TaskID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String taskid= documentSnapshot.getString("TaskID");
                    String taskname = documentSnapshot.getString("TaskName");
                    String startdate=documentSnapshot.getString("StartDate");
                    String finishdate=documentSnapshot.getString("FinishDate");
                    Double co=documentSnapshot.getDouble("TaskCost");
                    taskID.setText(taskid);
                    taskName.setText(taskname);
                    startDate.setText(startdate);
                    finishDate.setText(finishdate);
                    taskCost.setText(co+"");
                }

            });
            member m;
            ArrayList<member> mm = new ArrayList<member>();
            CollectionReference documentReference2 = db.collection("Task").document(TaskID).collection("Resources");
            documentReference2.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable QuerySnapshot snapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("", "listen:error", e);
                        return;
                    }
                    for (QueryDocumentSnapshot document : snapshots) {
                                     String ResourceName = document.getString("ResourceName");
                                     String ResourceType= document.getString("ResourceType");
                                     mm.add(new member(ResourceType,ResourceName));
                    }
                        for (int i = 0; i < mm.size(); i++) {
                                    TextView ch = new TextView(getApplicationContext());
                                    ch.setPadding(5, 0, 5, 0);
                                    ch.setMaxLines(10);
                                    ch.setTextColor(Color.parseColor("#616161"));
                                    ch.setTypeface(null, Typeface.BOLD_ITALIC);
                                    ch.setTextSize(18);
                                    ch.setText("Resource Type: "+mm.get(i).Type+", Resource Name: "+mm.get(i).Name);
                                    member.addView(ch);
                                }
                }
            });


//            Query q1 = db.collection("Task").document(TaskID).collection("Resources");
//            q1.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                     String ResourceName = document.getString("ResourceName");
//                                     String ResourceType= document.getString("ResourceType");
//                                     mm.add(new member(ResourceType,ResourceName));
//                                }
//
//                                for (int i = 0; i < mm.size(); i++) {
//                                    TextView ch = new CheckBox(getApplicationContext());
//                                    ch.setTextColor(Color.parseColor("#616161"));
//                                    ch.setTextSize(19);
//                                    ch.setText("Resource Type: "+mm.get(i).Type+", Resource Name: "+mm.get(i).Name);
//                                    member.addView(ch);
//                                }
//
//                            } else {
//                            }
//                        }
//
//                    });

        }
    }

    public void backToViewResourcePage(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            final String ResourceID = (String) intent1.getString("TaskID");
            Intent intent = new Intent(task_details.this, task_list.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            b.putString("TaskID",ResourceID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void OpenPupUp(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            final String ResourceID = (String) intent1.getString("TaskID");
            Intent intent = new Intent(task_details.this, WarningPupUpOfTask.class);
            Bundle b = new Bundle();
            b.putString("ProjectID",ProjectID);
            b.putString("TaskID",ResourceID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}

class member{
    String Type;
    String Name;

    public member(String type, String name) {
        Type = type;
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}