package com.jn769.remindme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class AddReminder extends AppCompatActivity {


    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private final Reminder reminder = new Reminder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final com.getbase.floatingactionbutton.FloatingActionButton fabDoneAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDoneAdd);
        assert fabDoneAdd != null;
        fabDoneAdd.setVisibility(View.VISIBLE);
        fabDoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderInfo();
            }
        });

        setupActionBar();

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);

        selectDate();
        selectTime();

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    fabDoneAdd.setVisibility(View.INVISIBLE);
                } else {
                    fabDoneAdd.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setReminderInfo() {
        EditText titleInput = (EditText) findViewById(R.id.titleEditText);
        EditText timeInput = (EditText) findViewById(R.id.timeEditText);
        EditText dateInput = (EditText) findViewById(R.id.dateEditText);
        EditText descInput = (EditText) findViewById(R.id.descEditText);

        assert titleInput != null;
        if (titleInput.getText().toString().isEmpty()) {

            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();

        } else {

            reminder.setTitle(titleInput.getText().toString());
            reminder.setTime(timeInput != null ? timeInput.getText().toString() : null);
            reminder.setDate(dateInput != null ? dateInput.getText().toString() : null);
            reminder.setDescription(descInput != null ? descInput.getText().toString() : null);

            DatabaseHandler db = new DatabaseHandler(this);
            db.addReminder(reminder);
            finish();

        }

    }

    private void selectTime() {
        final EditText setTime = (EditText) findViewById(R.id.timeEditText);
        assert setTime != null;
        setTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker = new TimePickerDialog(AddReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                        setTime.setText(timeFormatter.format(mcurrentTime.getTime()));
                    }
                }, hour, minute, false);
                mTimePicker.show();

            }
        });
    }

    private void selectDate() {

        final EditText setDate = (EditText) findViewById(R.id.dateEditText);
        assert setDate != null;
        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddReminder.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        mcurrentDate.set(selectedYear, selectedMonth, selectedDay);
                        setDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });
    }

    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}


