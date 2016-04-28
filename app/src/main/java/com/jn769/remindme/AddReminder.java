package com.jn769.remindme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class AddReminder extends AppCompatActivity {

    Context context;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private final Reminder reminder = new Reminder();

    Calendar mCalendarSet;
    int hour;
    int minute;
    int seconds;
    int mYear;
    int mMonth;
    int mDay;
    private int mId;


    AlarmManager alarmMgr;
    PendingIntent pendingIntent;
    Alarm alarm;
    protected Intent notificationIntent;
    Intent resultIntent;


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
                scheduleNotification();
            }
        });

        setupActionBar();

        mCalendarSet = new GregorianCalendar();

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

    public void selectTime() {
        final EditText setTime = (EditText) findViewById(R.id.timeEditText);
        assert setTime != null;
        setTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarSet = Calendar.getInstance();
                hour = mCalendarSet.get(Calendar.HOUR_OF_DAY);
                minute = mCalendarSet.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker = new TimePickerDialog(AddReminder.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour,
                                          int selectedMinute) {
                        mCalendarSet.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mCalendarSet.set(Calendar.MINUTE, selectedMinute);
                        setTime.setText(timeFormatter.format(mCalendarSet.getTime()));
                        hour = mCalendarSet.getTime().getHours();
                        minute = mCalendarSet.getTime().getMinutes();
                        seconds = mCalendarSet.getTime().getSeconds();

                        Log.d("MCALENDAR--->", "time: " + mCalendarSet.getTime().getHours()
                                + ':' + mCalendarSet.getTime().getMinutes());
                    }
                }, hour, minute, false);
                mTimePicker.show();

            }
        });
    }

    public void selectDate() {

        final EditText setDate = (EditText) findViewById(R.id.dateEditText);
        assert setDate != null;
        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarSet = Calendar.getInstance();
                mYear = mCalendarSet.get(Calendar.YEAR);
                mMonth = mCalendarSet.get(Calendar.MONTH);
                mDay = mCalendarSet.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddReminder.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        mCalendarSet.set(selectedYear, selectedMonth, selectedDay);
                        setDate.setText(dateFormatter.format(mCalendarSet.getTime()));
                        mYear = mCalendarSet.get(Calendar.YEAR);
                        mMonth = mCalendarSet.get(Calendar.MONTH);
                        mDay = mCalendarSet.get(Calendar.DAY_OF_MONTH);
                        Log.d("MCALENDAR--->", mCalendarSet.get(Calendar.YEAR) + " " + mCalendarSet.get(Calendar.MONTH) + " " + mCalendarSet.get(Calendar.DAY_OF_MONTH));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });
    }

    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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

    public void scheduleNotification() {

        context = getApplicationContext();

        mCalendarSet = Calendar.getInstance();
        mCalendarSet.setTimeInMillis(System.currentTimeMillis());
        mCalendarSet.set(Calendar.HOUR_OF_DAY, hour);
        mCalendarSet.set(Calendar.MINUTE, minute);
        mCalendarSet.set(Calendar.SECOND, seconds);
        Log.d("MCALENDAR--->", "hour: " + hour + '\n' + "minute: " + minute + '\n' + "seconds: " + seconds + '\n' + "year: " + mYear + '\n' + "month: " + mMonth + '\n' + "day: " + mDay);
        mCalendarSet.set(mYear, mMonth, mDay, hour, minute, seconds);
        Log.d("MCMillis", String.valueOf(mCalendarSet.getTimeInMillis()));
        Log.d("SystemMillis", String.valueOf(System.currentTimeMillis()));

        notificationIntent = new Intent(context, Alarm.class);

        notificationIntent.putExtra(Alarm.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(Alarm.NOTIFICATION, notification());

        pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, mCalendarSet.getTimeInMillis(), pendingIntent);

    }

    private Notification notification() {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(reminder.getTitle())
                .setContentText(reminder.getTime())
                .setSound(Uri.parse("content://settings/system/notification_sound"))
                .setAutoCancel(true);
        Log.d("Add Reminder", "------------>Created Notification<-------------");

        resultIntent = new Intent(this, MainActivity.class);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);

        return builder.build();

    }

}


