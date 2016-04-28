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
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditReminder extends AppCompatActivity {

    Context context;
    public Reminder reminder;
    public AddReminder addReminder;

    public RecyclerView.Adapter adapter;
    private final DatabaseHandler db = new DatabaseHandler(this);

    AlarmManager alarmMgr;
    PendingIntent pendingIntent;
    Alarm alarm;
    protected Intent notificationIntent;
    Intent resultIntent;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    Calendar mCalendarSet;
    int hour;
    int minute;
    int seconds;
    int mYear;
    int mMonth;
    int mDay;

    EditText title;
    EditText date;
    EditText time;
    EditText description;

    FloatingActionsMenu fabMenu;
    com.getbase.floatingactionbutton.FloatingActionButton fabSave;
    com.getbase.floatingactionbutton.FloatingActionButton fabCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabSave = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabSave);
        fabCancel = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabCancel);

        title = (EditText) findViewById(R.id.titleEditText);
        time = (EditText) findViewById(R.id.timeEditText);
        date = (EditText) findViewById(R.id.dateEditText);
        description = (EditText) findViewById(R.id.descEditText);

        mCalendarSet = Calendar.getInstance();
        hour = mCalendarSet.get(Calendar.HOUR_OF_DAY);
        minute = mCalendarSet.get(Calendar.MINUTE);
        mYear = mCalendarSet.get(Calendar.YEAR);
        mMonth = mCalendarSet.get(Calendar.MONTH);
        mDay = mCalendarSet.get(Calendar.DAY_OF_MONTH);

        Bundle reminderID = getIntent().getExtras();
        reminder = db.getReminder(reminderID.getInt("id"));

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);

        fabSave.setVisibility(View.VISIBLE);
        fabCancel.setVisibility(View.VISIBLE);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminderInfo();
                db.updateReminder(reminder);
                setAlarm();
                finish();
            }
        });

        getReminderInfo();
        selectDate();
        selectTime();

        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    assert fabMenu != null;
                    fabMenu.setVisibility(View.INVISIBLE);
                } else {
                    assert fabMenu != null;
                    fabMenu.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getReminderInfo() {

        assert title != null;
        title.setText(reminder.getTitle());
        assert date != null;
        date.setText(reminder.getDate());
        assert time != null;
        time.setText(reminder.getTime());
        assert description != null;
        description.setText(reminder.getDescription());
    }

    private void setReminderInfo() {

        if (title.getText().toString().isEmpty()) {

            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();

        } else {

            assert title != null;
            reminder.setTitle(title.getText().toString());
            reminder.setDate(date.getText().toString());
            reminder.setTime(time.getText().toString());
            reminder.setDescription(description.getText().toString());

        }

    }

    private void selectTime() {
        final EditText setTime = (EditText) findViewById(R.id.timeEditText);
        assert setTime != null;
        setTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCalendarSet = Calendar.getInstance();
                hour = mCalendarSet.get(Calendar.HOUR_OF_DAY);
                minute = mCalendarSet.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker = new TimePickerDialog(EditReminder.this,
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

    private void selectDate() {

        final EditText setDate = (EditText) findViewById(R.id.dateEditText);
        assert setDate != null;
        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarSet = Calendar.getInstance();
                mYear = mCalendarSet.get(Calendar.YEAR);
                mMonth = mCalendarSet.get(Calendar.MONTH);
                mDay = mCalendarSet.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditReminder.this, new DatePickerDialog.OnDateSetListener() {
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

    public void setAlarm() {

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

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

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
