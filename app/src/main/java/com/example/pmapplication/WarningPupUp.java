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

public class WarningPupUp extends Activity {
    Button yes,no;
    Bundle intent1;
    FirebaseFirestore db;
    String cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_pup_up);
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
                    final String ResourceID = (String) intent1.getString("ResourceID");
                    Intent intent = new Intent(WarningPupUp.this, resourceDetails.class);
                    Bundle b =new Bundle();
                    b.putString("ProjectID",ProjectID);
                    b.putString("ResourceID",ResourceID);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

    }

    public void deleteResource(View view) {

        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            final String ResourceID = (String) intent1.getString("ResourceID");
            Intent intent = new Intent(WarningPupUp.this, ViewResources.class);
            Bundle b = new Bundle();
            b.putString("ProjectID",ProjectID);
            b.putString("ResourceID",ResourceID);
            intent.putExtras(b);
            DocumentReference documentReference = db.collection("Resources").document(ResourceID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                     String cost = documentSnapshot.getString("Cost");
                    db.collection("Resources").document(ResourceID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DocumentReference docRef = db.collection("Projects").document(ProjectID);

                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String ProjectTotalCost= document.getString("TotalCost");
                                           Double TotalCost=Double.parseDouble(ProjectTotalCost);
                                            if(cost!=null ){
                                                Double ResCost=Double.parseDouble(cost);
                                                if(TotalCost>ResCost){
                                                    Double NewTotalCost=TotalCost-ResCost;
                                                    docRef.update("TotalCost", NewTotalCost+"").addOnSuccessListener(new OnSuccessListener<Void>(){

                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(WarningPupUp.this, "The Resource Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }else {
                                                    docRef.update("TotalCost", "0.0").addOnSuccessListener(new OnSuccessListener<Void>(){

                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(WarningPupUp.this, "The Resource Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }
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
                            Toast.makeText(WarningPupUp.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        }
    }
}

