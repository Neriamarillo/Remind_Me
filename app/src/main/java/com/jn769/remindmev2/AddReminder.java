package com.jn769.remindmev2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

//import android.support.v7.app.NotificationCompat;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class AddReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private ReminderViewModel reminderViewModel;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private Date dateSet;

    private TextInputLayout titleInput;
    private TextInputEditText titleEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText descEditText;

    private MaterialButton saveButton;
    private MaterialButton cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

        // Date and time
        dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        calendar = Calendar.getInstance();
        dateListener();
        timeListener();

        titleInput = findViewById(R.id.titleInput);
        titleEditText = findViewById(R.id.titleEditText);
        timeEditText = findViewById(R.id.timeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        descEditText = findViewById(R.id.descEditText);

        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTitleValid(titleEditText.getText())) {
                    titleInput.setError(getString(R.string.titleError));
                    titleEditText.setError(getString(R.string.titleError));
                } else {
                    setReminderInfo();
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean isTitleValid(@Nullable Editable text) {
        return text != null && text.length() >= 1;
    }

    private void setReminderInfo() {
        reminderViewModel.insert(new Reminder(
                String.valueOf(titleEditText.getText()),
                String.valueOf(timeEditText.getText()),
                dateSet,
                String.valueOf(descEditText.getText())));
    }

    // Time Selection Listener
    public void timeListener() {
        final TextInputEditText setTime = findViewById(R.id.timeEditText);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime();

            }
        });
        setTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });
    }

    // Time setup
    public void setUpTime() {
        final TextInputEditText getTime = findViewById(R.id.timeEditText);
        if (Objects.requireNonNull(getTime.getText()).length() > 0) {
            timePickerDialog.show();
        } else {
            timePickerDialog = new TimePickerDialog(
                    this,
                    AddReminder.this,
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    false);
            timePickerDialog.show();
        }
    }

    public void onTimeSet(TimePicker timePicker,
                          int selectedHour,
                          int selectedMinute) {
        final Date time;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        time = calendar.getTime();
        final TextInputEditText timeEditText = findViewById(R.id.timeEditText);
        String timeString = timeFormatter.format(time);
        timeEditText.setText(timeString);
        timeEditText.setShowSoftInputOnFocus(false);
    }

    // Date Selection Listener
    public void dateListener() {
        final TextInputEditText setDate = findViewById(R.id.dateEditText);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDate();
            }
        });
        setDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });
    }

    // Date setup
    public void setUpDate() {
        final TextInputEditText getDate = findViewById(R.id.dateEditText);
        if (Objects.requireNonNull(getDate.getText()).length() > 0) {
            datePickerDialog.show();
        } else {
            datePickerDialog = new DatePickerDialog(
                    this,
                    AddReminder.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    public void onDateSet(DatePicker datepicker,
                          int selectedYear,
                          int selectedMonth,
                          int selectedDay) {
        final Date date;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        date = calendar.getTime();
        final TextInputEditText dateEditText = findViewById(R.id.dateEditText);
        String dateString = dateFormatter.format(date);
        dateEditText.setText(dateString);
        dateSet = date;
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}


