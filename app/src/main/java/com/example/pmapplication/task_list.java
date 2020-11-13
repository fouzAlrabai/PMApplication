package com.example.pmapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

    public class task_list extends AppCompatActivity {
        Bundle intent1;
        FirebaseAuth mAuth;
        FirebaseFirestore db;
        Task task;
        ListView listView;
        ArrayList<Task> Tasks;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_task_list);
            mAuth = FirebaseAuth.getInstance();
            listView = findViewById(R.id.TaskList);
            Tasks = new ArrayList<Task>();
            db = FirebaseFirestore.getInstance();
            intent1 = getIntent().getExtras();
            if (intent1 != null) {
                final String ProjectID = (String) intent1.getString("ProjectID");
                db.collection("Task")
                        .whereEqualTo("ProjectID", ProjectID)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot snapshots,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("", "listen:error", e);
                                    return;
                                }
                                for (QueryDocumentSnapshot document : snapshots) {
                                    String projectID= document.getString("ProjectID");
                                    String TaskName = document.getString("TaskName");
                                    String StartDate=document.getString("StartDate");
                                    String TaskID=document.getString("TaskID");
                                   String FinishDate=document.getString("FinishDate");
                                    task = new Task(document.getId(), projectID, TaskName, TaskID,StartDate,FinishDate);
                                            Tasks.add(task);
                                    task_list.MyTasksAdapter myTasksAdapter = new MyTasksAdapter(task_list.this, R.layout.activity_single_task, Tasks);
                                    listView.setAdapter(myTasksAdapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Task temp = (Task) parent.getItemAtPosition(position);
                                            Intent intent = new Intent(task_list.this, task_details.class);
                                            Bundle b =new Bundle();
                                            b.putString("TaskID",temp.getID());
                                            b.putString("ProjectID",temp.getProjectID());
                                            intent.putExtras(b);
                                            startActivity(intent);
                                        }
                                    });

                                }

                            }// end if null
                        });
            }
        }


        public void backToProjectDetailsPage(View view) {
            intent1 = getIntent().getExtras();
            if (intent1 != null) {
                final String ProjectID = (String) intent1.getString("ProjectID");
                Intent intent = new Intent(com.example.pmapplication.task_list.this, projectDetails.class);
                Bundle b =new Bundle();
                b.putString("ProjectID",ProjectID);
                intent.putExtras(b);
                startActivity(intent);
            }
        }

        public void openAddTask(View view) {
            intent1 = getIntent().getExtras();
            if (intent1 != null) {
                final String ProjectID = (String) intent1.getString("ProjectID");
                Intent intent = new Intent(task_list.this, activity_add_task.class);
                Bundle b =new Bundle();
                b.putString("ProjectID",ProjectID);
                intent.putExtras(b);
                startActivity(intent);
            }
        }


        //adapter

        class MyTasksAdapter extends BaseAdapter {

            private Context context;
            ArrayList<Task> Tasks;
            int layoutTaskId;

            MyTasksAdapter(Context context, ArrayList<Task> tasks) {
                this.Tasks = tasks;
                this.context = context;
            }

            public MyTasksAdapter(Context context, int activity_single_task, ArrayList<Task> Tasks) {
                this.Tasks = Tasks;
                this.context = context;
                this.layoutTaskId = activity_single_task;
            }

            public ArrayList<Task> getAll(){ return Tasks;}

            @Override
            public int getCount() {
                return Tasks.size();
            }

            @Override
            public Object getItem(int position) {
                return Tasks.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.activity_single_task, null);
                TextView nameTask = (TextView) view.findViewById(R.id.nameTask);
                TextView idTask = (TextView) view.findViewById(R.id.idTask);
                nameTask.setText(Tasks.get(position).TaskName);
                idTask.setText(Tasks.get(position).TaskID);
                return view;
            }
        }
    }



      //**************Task class
         class Task {
            String ID ,ProjectID,TaskName,TaskID,StartDate,FinishDate;

            public Task(String ID, String projectID,String taskName ,String taskID,String startDate, String finishDate) {
                this.ID=ID;
                this.ProjectID = projectID;
                TaskName = taskName;
                TaskID = taskID;
                StartDate=startDate;
                FinishDate=finishDate;
            }

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public void setStartDate(String startDate) {
                StartDate = startDate;
            }

            public void setFinishDate(String finishDate) {
                FinishDate = finishDate;
            }
            public String getStartDate() {
                return StartDate;
            }

            public String getFinishDate() {
                return FinishDate;
            }


            public String getProjectID() {
                return ProjectID;
            }

            public void setProjectID(String projectID) {
                ProjectID = projectID;
            }

            public String getTaskName() {
                return TaskName;
            }

            public void setTaskName(String taskName) {
                TaskName = taskName;
            }

            public String getTaskID() {
                return TaskID;
            }

            public void setTaskID(String taskID) {
                TaskID = taskID;
            }


        }


