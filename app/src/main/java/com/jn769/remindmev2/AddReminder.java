package com.jn769.remindmev2;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class AddReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final String CHANNEL_ID = "Reminders";

    private ReminderViewModel reminderViewModel;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private Calendar calendar;
    private String dateString;
    private String timeString;

    private TextInputLayout titleInput;
    private TextInputEditText titleEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText descEditText;
    private LinearLayout timeDate;

    private RevealAnimation revealAnimation;

    private boolean notificationRequested = false;
    private long alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.addToolbar);
        toolbar.setTitle(R.string.title_activity_add_reminder);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnimation.unRevealActivity();
            }
        });

        createNotificationChannel();
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

        // Animation
        Intent intent = this.getIntent();   //get the intent to receive the x and y coords, that you passed before

        //there you have to get the root layout of your second activity
        CircularRevealCoordinatorLayout rootLayout = findViewById(R.id.addReminder);
        revealAnimation = new RevealAnimation(rootLayout, intent, this);

        // Date and time
        dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        calendar = Calendar.getInstance();

        titleInput = findViewById(R.id.titleInput);
        titleEditText = findViewById(R.id.titleEditText);
        timeEditText = findViewById(R.id.timeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        descEditText = findViewById(R.id.descEditText);
        timeDate = findViewById(R.id.timeDateLand);
        MaterialButton saveButton = findViewById(R.id.save_button);
        MaterialButton cancelButton = findViewById(R.id.cancel_button);
        MaterialCheckBox materialCheckBox = findViewById(R.id.checkbox_notify);

        titleEditText.requestFocus();
        timeDate.setVisibility(View.GONE);

        dateListener();
        timeListener();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTitleValid(titleEditText.getText())) {
                    titleInput.setError(getString(R.string.titleError));
                    titleEditText.setError(getString(R.string.titleError));
                } else {
                    setReminderInfo();
                    if (notificationRequested) {
                        startNotification();
                    }

                    revealAnimation.unRevealActivity();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealAnimation.unRevealActivity();
            }
        });

        Log.d("NOTIFICATION REQUESTED 1", String.valueOf(notificationRequested));
        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeDate.setVisibility(View.VISIBLE);
                    notificationRequested = true;
//                    Log.d("NOTIFICATION REQUESTED 2", String.valueOf(notificationRequested));

                } else {
                    timeDate.setVisibility(View.GONE);
                    notificationRequested = false;
//                    Log.d("NOTIFICATION REQUESTED 3", String.valueOf(notificationRequested));

                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String groupId = "com.jn769.remindmev2";
            String groupName = "Reminders";

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
        }

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminders";
            String description = "Reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onBackPressed() {
        revealAnimation.unRevealActivity();
    }

    private boolean isTitleValid(@Nullable Editable text) {
        return text != null && text.length() >= 1;
    }

    private void setReminderInfo() {
        if (notificationRequested) {
            alarmId = (int) System.currentTimeMillis();
        } else {
            alarmId = 0;
        }
        reminderViewModel.insert(new Reminder(
                String.valueOf(titleEditText.getText()),
                String.valueOf(timeEditText.getText()),
                String.valueOf(dateEditText.getText()),
                String.valueOf(descEditText.getText()),
                alarmId));

    }

    // Time Selection Listener
    private void timeListener() {
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime();

            }
        });
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                }
            }
        });
    }

    // Time setup
    private void setUpTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                AddReminder.this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker timePicker,
                          int selectedHour,
                          int selectedMinute) {
        final Date time;
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        time = calendar.getTime();
        timeString = timeFormatter.format(time);
        timeEditText.setText(timeString);
        timeEditText.setShowSoftInputOnFocus(false);

    }

    // Date Selection Listener
    private void dateListener() {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDate();
            }
        });
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                }
            }
        });
    }

    // Date setup
    private void setUpDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                AddReminder.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void onDateSet(DatePicker datepicker,
                          int selectedYear,
                          int selectedMonth,
                          int selectedDay) {
        final Date date;
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        date = calendar.getTime();
        dateString = dateFormatter.format(date);
        Log.d("FORMATTED DATE", dateString);
        dateEditText.setText(dateString);
    }

    //    NOTIFICATION
    private void startNotification() {
        if (dateString != null || timeString != null) {
            AlarmHandler alarmHandler = new AlarmHandler(getApplication());
            Alarm alarm = new Alarm(
                    Objects.requireNonNull(titleEditText.getText()).toString(),
                    Objects.requireNonNull(descEditText.getText()).toString(),
                    alarmId,
                    calendar.getTimeInMillis());
            alarmHandler.addAlarm(alarm);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
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


