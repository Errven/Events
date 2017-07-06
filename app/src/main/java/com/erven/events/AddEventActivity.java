package com.erven.events;

import android.app.DatePickerDialog;
import android.app.Dialog;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private Button buttonAdd;
    private EditText nameText;
    private EditText placeText;
    private EditText dateText;
    private EditText timeText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    int yearSet, monthSet, daySet;
    int hourSet, minuteSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        nameText = (EditText) findViewById(R.id.nameText);
        placeText = (EditText) findViewById(R.id.placeText);
        dateText = (EditText) findViewById(R.id.dateText);
        timeText = (EditText) findViewById(R.id.timeText);

        buttonAdd.setOnClickListener(this);
        dateText.setOnClickListener(this);
        timeText.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        yearSet = cal.get(Calendar.YEAR);
        monthSet = cal.get(Calendar.MONTH);
        daySet = cal.get(Calendar.DAY_OF_MONTH);
        hourSet = cal.get(Calendar.HOUR);
        minuteSet = cal.get(Calendar.MINUTE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            addEvent();
        } else if (v == dateText) {
            showDialog(0);
        } else if (v == timeText) {
            showDialog(1);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0)
            return new DatePickerDialog(this, datePickerListener, yearSet, monthSet, daySet);
        if (id == 1)
            return new TimePickerDialog(this, timePickerListener, hourSet, minuteSet, true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearSet = year;
            monthSet = month + 1;
            daySet = dayOfMonth;

            dateText.setText(String.format("%d/%d/%d", daySet, monthSet, yearSet));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourSet = hourOfDay;
            minuteSet = minute;
            timeText.setText(String.format("%d:%d", hourSet, minuteSet));
            if (minuteSet < 0)
                timeText.setText(String.format("%d:0%d", hourSet, minuteSet));
        }
    };

    private void addEvent() {
        String name = nameText.getText().toString();
        String place = placeText.getText().toString();
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        String owner = firebaseAuth.getCurrentUser().getEmail();
        String id = getID();
        ArrayList<String> users = new ArrayList<>();

        Event event = new Event(id, name, place, date, time, owner, users);

        databaseReference.child("events").child(event.id).setValue(event);
        showToast("Added to database");
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected String getID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 10) {
            int index = (int) (rnd.nextFloat() * chars.length());
            string.append(chars.charAt(index));
        }
        return string.toString();

    }
}
