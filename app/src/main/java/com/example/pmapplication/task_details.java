package com.example.pmapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class task_details extends AppCompatActivity {
    Bundle intent1;
    TextView taskID,taskName,startDate,finishDate ;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
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
        params.x=0;
        params.y=-40;
        getWindow().setAttributes(params);
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        taskID=findViewById(R.id.taskId);
        taskName=findViewById(R.id.taskName);
        startDate=findViewById(R.id.startDate);
        finishDate=findViewById(R.id.finishDate);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String TaskID = (String) intent1.getString("TaskID");
            DocumentReference documentReference = db.collection("Task").document(TaskID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String cost = documentSnapshot.getString("Cost");
                    String taskname = documentSnapshot.getString("TaskName");
                    String taskid= documentSnapshot.getString("TaskID");
                    String startdate=documentSnapshot.getString("StartDate");
                    String finishdate=documentSnapshot.getString("FinishDate");
                    taskName.setText(taskname);
                    taskID.setText(taskid);
                    startDate.setText(startdate);
                    finishDate.setText(finishdate);
                }

            });
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
            Intent intent = new Intent(task_details.this, WarningPupUp.class);
            Bundle b = new Bundle();
            b.putString("ProjectID",ProjectID);
            b.putString("TaskID",ResourceID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}