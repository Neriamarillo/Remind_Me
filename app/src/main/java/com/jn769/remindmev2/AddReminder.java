package com.jn769.remindmev2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

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
    private MaterialCheckBox materialCheckBox;

    private RevealAnimation revealAnimation;

    private NotificationManager notificationManager;
    private boolean notificationRequested;
    private int alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.addToolbar);
        toolbar.setTitle(R.string.title_activity_add_reminder);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
        materialCheckBox = findViewById(R.id.checkbox_notify);

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

        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeDate.setVisibility(View.VISIBLE);
                    notificationRequested = true;
                } else {
                    timeDate.setVisibility(View.GONE);
                    notificationRequested = false;
                }
            }
        });

        // Notifications


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String groupId = "com.jn769.remindmev2";
            String groupName = "Reminders";

//            INTENT FOR NOTIFICATION SETTINGS

//            Intent notifIntent = null;
//            notifIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            notifIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//            notifIntent.putExtra(Settings.EXTRA_CHANNEL_ID, groupId);
//            startActivity(notifIntent);

            notificationManager =
                    (NotificationManager)
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

    private void startNotification() {
        if (dateString != null || timeString != null) {

            Intent notifyIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);


            notifyIntent.putExtra("title", Objects.requireNonNull(titleEditText.getText()).toString());
            notifyIntent.putExtra("description", Objects.requireNonNull(descEditText.getText()).toString());
            notifyIntent.putExtra("alarmId", alarmId);
            Log.d("title", titleEditText.getText().toString());
            Log.d("ALARMID", String.valueOf(alarmId));

            PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                    (getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifyPendingIntent);
                Toast.makeText(this, "Event scheduled at " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR), Toast.LENGTH_LONG).show();
            } else {
                alarmManager.cancel(notifyPendingIntent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        revealAnimation.unRevealActivity();
    }

    private boolean isTitleValid(@Nullable Editable text) {
        return text != null && text.length() >= 1;
    }

    void setReminderInfo() {
        alarmId = (int) System.currentTimeMillis();
        reminderViewModel.insert(new Reminder(
                String.valueOf(titleEditText.getText()),
                String.valueOf(timeEditText.getText()),
                String.valueOf(dateEditText.getText()),
                String.valueOf(descEditText.getText()),
                alarmId));
    }

    // Time Selection Listener
    public void timeListener() {
//        final TextInputEditText setTime = findViewById(R.id.timeEditText);
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
    public void setUpTime() {
        //                R.style.RemindMe_Picker,
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
//                R.style.RemindMe_Picker,
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
//        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        time = calendar.getTime();
//        timeSet = time;
//        Log.d("TIME", timeSet.toString());
        timeString = timeFormatter.format(time);
        timeEditText.setText(timeString);
        timeEditText.setShowSoftInputOnFocus(false);

    }

    // Date Selection Listener
    public void dateListener() {
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
    public void setUpDate() {
        //                R.style.RemindMe_Picker,
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
//                R.style.RemindMe_Picker,
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
//        dateSet = date;
//        Log.d("DATE", String.valueOf(dateSet));
//        final TextInputEditText dateEditText = findViewById(R.id.dateEditText);
        dateString = dateFormatter.format(date);
        Log.d("FORMATTED DATE", dateString);
        dateEditText.setText(dateString);
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


