package com.example.pmapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

public class ViewResources extends AppCompatActivity {
    Bundle intent1;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Resource resource;
    ListView listView;
    ArrayList<Resource> Resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resources);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.resourceList);
        Resources = new ArrayList<Resource>();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            db.collection("Resources")
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
                                String Cost = document.getString("Cost");
                                String projectID= document.getString("ProjectID");
                                String ResourceName = document.getString("ResourceName");
                                String ResourceType=document.getString("ResourceType");
                                String TimePerDay=document.getString("TimePerDay");
                                resource = new Resource(document.getId(),Cost, projectID, ResourceName, ResourceType,TimePerDay);
                                Resources.add(resource);
                                MyResourcesAdapter myResourcesAdapter = new MyResourcesAdapter(ViewResources.this, R.layout.activity_single_resource, Resources);
                                listView.setAdapter(myResourcesAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Resource temp = (Resource) parent.getItemAtPosition(position);
                                        Intent intent = new Intent(ViewResources.this, resourceDetails.class);
                                        Bundle b =new Bundle();
                                        b.putString("ResourceID",temp.getID());
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
            Intent intent = new Intent(ViewResources.this, projectDetails.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void OpenAddResourcePage(View view) {
        intent1 = getIntent().getExtras();
        if (intent1 != null) {
            final String ProjectID = (String) intent1.getString("ProjectID");
            Intent intent = new Intent(ViewResources.this, AddResource.class);
            Bundle b =new Bundle();
            b.putString("ProjectID",ProjectID);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    class MyResourcesAdapter extends BaseAdapter {

        private Context context;
        ArrayList<Resource> resources;
        int layoutResourseId;

        MyResourcesAdapter(Context context, ArrayList<Resource> resources) {
            this.resources = resources;
            this.context = context;
        }

        public MyResourcesAdapter(Context context, int activity_single_request, ArrayList<Resource> resources) {
            this.resources = resources;
            this.context = context;
            this.layoutResourseId = activity_single_request;
        }

        public ArrayList<Resource> getAll(){ return resources;}

        @Override
        public int getCount() {
            return resources.size();
        }

        @Override
        public Object getItem(int position) {
            return resources.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_single_resource, null);
            TextView ResourceName = (TextView) view.findViewById(R.id.ResourceName);
            TextView ResourceType = (TextView) view.findViewById(R.id.ResourceType);
            ResourceName.setText(resources.get(position).ResourceName);
            ResourceType.setText(resources.get(position).ResourceType);
            return view;
        }
    }
}