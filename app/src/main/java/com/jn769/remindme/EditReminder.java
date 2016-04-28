package com.jn769.remindme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    Calendar mCalendarSet;
    int hour;
    int minute;
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
//                setAlarm(editedNotification());
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
            Context context = getApplicationContext();
            CharSequence text = "Need to set a title";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
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


                final TimePickerDialog mTimePicker = new TimePickerDialog(EditReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mCalendarSet.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mCalendarSet.set(Calendar.MINUTE, selectedMinute);
                        setTime.setText(timeFormatter.format(mCalendarSet.getTime()));
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
                final Calendar mCalendarSet = Calendar.getInstance();

                DatePickerDialog mDatePicker = new DatePickerDialog(EditReminder.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        mCalendarSet.set(selectedYear, selectedMonth, selectedDay);
                        setDate.setText(dateFormatter.format(mCalendarSet.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });
    }
//
//    public void setAlarm(NotificationManager notification) {
//
//
//        Intent notificationIntent = new Intent(this, Alarm.class);
//        notificationIntent.putExtra(Alarm.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(Alarm.NOTIFICATION, (Parcelable) notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
//
//        mCalendarSet = Calendar.getInstance();
//        mCalendarSet.set(Calendar.HOUR_OF_DAY, hour);
//        mCalendarSet.set(Calendar.MINUTE, minute);
//        mCalendarSet.set(Calendar.YEAR, mYear);
//        mCalendarSet.set(Calendar.MONTH, mMonth);
//        mCalendarSet.set(Calendar.DATE, mDay);
//        mCalendarSet.set(mYear, mMonth, mDay, hour, minute);
//
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, mCalendarSet.getTimeInMillis(), pendingIntent);
//    }
//
////    private void updateNotification() {
////        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        int notifyID = 1;
////        android.support.v4.app.NotificationCompat.Builder mNotifyBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
////                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
////                .setOnlyAlertOnce(true)
////                .setAutoCancel(true)
////                .setContentTitle(reminder.getTitle())
////                .setContentText(reminder.getTime())
////                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
////        return mNotificationManager.notify(
////                notifyID,
////                mNotifyBuilder.build());
////    }
//
//    public NotificationManager editedNotification() {
//        int notifyID = 1;
//        android.support.v4.app.NotificationCompat.Builder mNotifyBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
//                .setOnlyAlertOnce(true)
//                .setAutoCancel(true)
//                .setContentTitle(reminder.getTitle())
//                .setContentText(reminder.getTime())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        return mNotificationManager.notify(notifyID, mNotifyBuilder.build());
//
//
//    }

}
