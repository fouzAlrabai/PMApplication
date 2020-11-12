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
        import androidx.annotation.Nullable;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Map;

public class WarningPupUp extends Activity {
    Button yes,no;
    Bundle intent1;
    FirebaseFirestore db;

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
        // if user pressed yes

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent1 != null) {
                    final String ProjectID = (String) intent1.getString("ProjectID");
                    final String ResourceID = (String) intent1.getString("ResourceID");
                    Intent intent = new Intent(WarningPupUp.this, resourceDetails.class);
                    Bundle b =new Bundle();
                    b.putString("ProjectID",ProjectID);
                    b.putString("ResourceID",ResourceID);
                    intent.putExtras(b);
                    startActivity(intent);

//                    DocumentReference documentReference =db.collection("Resources").document(ResourceID);
//                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot documentSnapshot = task.getResult();
//                                if (documentSnapshot.exists()) {
//                                    String recourceCost = documentSnapshot.getString("Cost");
//                                    Double recCost=Double.parseDouble(recourceCost);
//                                    DocumentReference docRef = db.collection("Projects").document(ProjectID);
//                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                DocumentSnapshot document = task.getResult();
//                                                if (document != null && document.exists()) {
//                                                    String ProjectCost=document.getString("TotalCost");
//                                                    Double TotalCost=Double.parseDouble(ProjectCost);
//                                                    Double NewTotalCost=TotalCost-recCost;
//                                                    String Total=NewTotalCost+"";
//                                                    DocumentReference documentRef = db.collection("Projects").document(ProjectID);
//                                                    documentRef.update("TotalCost", Total).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
////                                            DocumentReference Ref = db.collection("Resources").document(ResourceID);
//                                                            db.collection("Resources").document(ResourceID).delete();
////                                            startActivity(intent);
////                                            Ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
////                                                @Override
////                                                public void onSuccess(Void aVoid) {
//////                                                    startActivity(intent);
////                                                }
////                                            });
//                                                        }
//                                                    });
//                                                } else {
//
//                                                }
//                                            } else {
//
//                                            }
//                                        }
//                                    });
//
//                                }
//                            }
//                        }
//                    });
                }
            }
        });
    }
}

