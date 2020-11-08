package com.example.pmapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import java.text.ParseException;
import java.util.Calendar;
import android.widget.DatePicker;
public class addProject extends AppCompatActivity {
    DatePickerDialog datepicker;
    EditText dateOfPickUp,dateOfPickUp2;
    Calendar calendar;
    int day,month,year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static final int REQUEST_CODE = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        dateOfPickUp = (EditText)findViewById(R.id.startDate);
        dateOfPickUp2 = (EditText)findViewById(R.id.endDate);
//        final FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
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
//        dateOfPickUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getBaseContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                dateOfPickUp.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });
    }

    private void showDateDialog() {
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateOfPickUp.setText(year+"/"+month+"/"+day);
            }
        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,listener,year,month,day);
        datePickerDialog.show();
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void backToHomePage(View view) {
        startActivity(new Intent(addProject.this,HomePage.class));
    }
}