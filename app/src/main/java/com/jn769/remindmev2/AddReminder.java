package com.jn769.remindmev2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import android.support.v7.app.NotificationCompat;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class AddReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    Context context;
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

        dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                AddReminder.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog = new TimePickerDialog(
                this,
                AddReminder.this,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                false);

        setUpDate();
        setUpTime();

        // Inflate the layout for this fragment
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


    // TODO: Fix date input to not allow it to be persistently set after the first time.


    private void setReminderInfo() {
        reminderViewModel.insert(new Reminder(
                String.valueOf(titleEditText.getText()),
                String.valueOf(timeEditText.getText()),
                dateSet,
                String.valueOf(descEditText.getText())));
    }

    // Time setup
    public void setUpTime() {
        final TextInputEditText setTime = findViewById(R.id.timeEditText);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
        setTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timePickerDialog.show();
                }
            }
        });

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
    }

    // Date setup
    public void setUpDate() {
        final TextInputEditText setDate = findViewById(R.id.dateEditText);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        setDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show();
                }
            }
        });
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

//    public void scheduleNotification() {
//
//
//        if (!reminder.getTime().equals("") && !reminder.getDate().equals("")) {
//
//            context = getApplicationContext();
//
//            mCalendarSet = Calendar.getInstance();
//            mCalendarSet.setTimeInMillis(System.currentTimeMillis());
//            mCalendarSet.set(Calendar.HOUR_OF_DAY, hour);
//            mCalendarSet.set(Calendar.MINUTE, minute);
//            mCalendarSet.set(Calendar.SECOND, seconds);
//            Log.d("MCALENDAR--->", "hour: " + hour + '\n' + "minute: " + minute + '\n' + "seconds: " + seconds + '\n' + "year: " + mYear + '\n' + "month: " + mMonth + '\n' + "day: " + mDay);
//            mCalendarSet.set(mYear, mMonth, mDay, hour, minute, seconds);
//            Log.d("MCMillis", String.valueOf(mCalendarSet.getTimeInMillis()));
//            Log.d("SystemMillis", String.valueOf(System.currentTimeMillis()));
//
//            notificationIntent = new Intent(context, Alarm.class);
//
////            notificationIntent.putExtra(Alarm.NOTIFICATION_ID, 1);
////            notificationIntent.putExtra(Alarm.NOTIFICATION, notification());
//
//            pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmMgr.set(AlarmManager.RTC_WAKEUP, mCalendarSet.getTimeInMillis(), pendingIntent);
//        } else {
//            Log.d("No notification created", "Notify");
//        }
//
//    }

//    private Notification notification() {
//        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
//                .setContentTitle(reminder.getTitle())
//                .setContentText(reminder.getTime())
//                .setSound(Uri.parse("content://settings/system/notification_sound"))
//                .setAutoCancel(true);
//        Log.d("Add Reminder", "------------>Created Notification<-------------");
//
//        resultIntent = new Intent(this, MainActivity.class);
//        PendingIntent notifyPendingIntent =
//                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(notifyPendingIntent);
//
//        return builder.build();
//
//    }

}


