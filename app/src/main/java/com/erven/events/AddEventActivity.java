package com.erven.events;

import android.app.DatePickerDialog;
import android.app.Dialog;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private Button buttonAdd;
    private EditText nameText;
    private EditText placeText;
    private EditText dateText;
    private EditText timeText;

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
            if(minuteSet<0)
                timeText.setText(String.format("%d:0%d", hourSet, minuteSet));
        }
    };

    private void addEvent() {
        showDialog(0);
    }
}
