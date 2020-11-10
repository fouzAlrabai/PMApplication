package com.example.pmapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class HomePage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String managerID, reqID;
    Project project;
    ListView listView;
    ArrayList<Project> projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.projectList);
        projects = new ArrayList<Project>();
        db = FirebaseFirestore.getInstance();
        managerID = mAuth.getCurrentUser().getUid();
        db.collection("Projects")
                .whereEqualTo("managerID", managerID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : snapshots) {
                            String ProjectName = document.getString("ProjectName");
                            String StartDate = document.getString("StartDate");
                            String FinishDate= document.getString("FinishDate");
                            String Budget=document.getString("Budget");
                            project = new Project(document.getId(),ProjectName, StartDate, FinishDate, Budget,managerID);
                            projects.add(project);
                            MyProjectsAdapter myProjectsAdapter = new MyProjectsAdapter(HomePage.this, R.layout.activity_single_project, projects);
                            listView.setAdapter(myProjectsAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Project temp = (Project) parent.getItemAtPosition(position);
//                                    Intent in = getIntent();
//                                    in.putExtra("ProjectID", temp.getID());
//                                    in.putExtra("Where","Projects");
                                    Intent intent = new Intent(HomePage.this, projectDetails.class);
//                                    intent.putExtra("ProjectID", in.getStringExtra("ProjectID"));
//                                    intent.putExtra("Where", in.getStringExtra("Where"));
                                    Bundle b =new Bundle();
                                    b.putString("ProjectID",temp.getID());
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            });
                        }

                    }// end if null
                });
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(HomePage.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomePage.this,  Login.class));
    }

    public void OpenAddProject(View view) {
        startActivity(new Intent(HomePage.this,  addProject.class));
    }

    class MyProjectsAdapter extends BaseAdapter {

        private Context context;
        ArrayList<Project> project;
        int layoutResourseId;

        MyProjectsAdapter(Context context, ArrayList<Project> project) {
            this.project = project;
            this.context = context;
        }

        public MyProjectsAdapter(Context context, int activity_single_request, ArrayList<Project> project) {
            this.project = project;
            this.context = context;
            this.layoutResourseId = activity_single_request;
        }

        public ArrayList<Project> getAll(){ return project;}

        @Override
        public int getCount() {
            return project.size();
        }

        @Override
        public Object getItem(int position) {
            return project.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_single_project, null);
            TextView projectName = (TextView) view.findViewById(R.id.projectName);
            TextView startDate = (TextView) view.findViewById(R.id.startDate);
            TextView finishDate = (TextView) view.findViewById(R.id.finishDate);
            projectName.setText(project.get(position).ProjectName);
            startDate.setText(project.get(position).StartDate);
            finishDate.setText(project.get(position).FinishDate);
            return view;
        }
    }
}