package com.example.pmapplication;

import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.view.Gravity;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.Toast;

        import androidx.annotation.NonNull;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Map;

import javax.annotation.Nullable;

    public class WarningPupUpOfTask extends Activity {
        Button yes,no;
        Bundle intent1;
        FirebaseFirestore db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_warning_pup_up_of_task);
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

            yes = (Button)findViewById(R.id.yes);
            no=(Button)findViewById(R.id.no);
            intent1 = getIntent().getExtras();
            db = FirebaseFirestore.getInstance();
            // if user pressed no
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                startActivity(new Intent(WarningPupUp.this,  resourceDetails.class));
                    if (intent1 != null) {
                        final String ProjectID = (String) intent1.getString("ProjectID");
                        final String TaskID = (String) intent1.getString("TaskID");
                        Intent intent = new Intent(WarningPupUpOfTask.this, task_details.class);
                        Bundle b =new Bundle();
                        b.putString("ProjectID",ProjectID);
                        b.putString("TaskID",TaskID);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });

        }

        public void deleteTask(View view) {

            intent1 = getIntent().getExtras();
            if (intent1 != null) {
                final String ProjectID = (String) intent1.getString("ProjectID");
                final String TaskID = (String) intent1.getString("TaskID");
                Intent intent = new Intent(WarningPupUpOfTask.this, task_list.class);
                Bundle b = new Bundle();
                b.putString("ProjectID",ProjectID);
                b.putString("TaskID",TaskID);
                intent.putExtras(b);
                        db.collection("Task").document(TaskID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(WarningPupUpOfTask.this, "The Task Deleted Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(WarningPupUpOfTask.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

