package com.jn769.remindmev2;


import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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


public class EditReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final String CHANNEL_ID = "Reminders";
    private static final int NOTIFICATION_ID = 0;


    private ReminderViewModel reminderViewModel;

    Bundle bundle;
    private int reminderId;
    private LiveData<Reminder> reminderLiveData;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat smallDateFormatter;
    private Calendar calendar;
    private Date dateSet;
    private String dateString;
    private String timeString;
    private Date timeSet;
    private Date selectedDate;

    private TextInputLayout titleInput;
    private TextInputEditText titleEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText descEditText;
    private LinearLayout timeDate;
    private MaterialCheckBox materialCheckBox;

    private RevealAnimation revealAnimation;

    private NotificationManager notificationManager;
    private long alarmId;
    private int alarmIdFromData;
    private boolean hasNotification;
    private boolean notificationRequested = false;

    public EditReminder() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_reminder);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.editToolbar);
        toolbar.setTitle(R.string.title_edit_reminder);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }


        EditReminderViewModel editReminderViewModel = ViewModelProviders.of(this).get(EditReminderViewModel.class);
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

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
        /*Buttons*/
        MaterialButton saveButton = findViewById(R.id.save_button);
        MaterialButton cancelButton = findViewById(R.id.cancel_button);
        materialCheckBox = findViewById(R.id.checkbox_notify);

        Log.d("DATE", String.valueOf(dateEditText.getText()));
        Log.d("TIME", String.valueOf(timeEditText.getText()));

//        materialCheckBox.setChecked(false);


        Intent intent = this.getIntent();

        bundle = intent.getExtras();

        if (bundle != null) {
            reminderId = bundle.getInt("REMINDER_ID");
        }

        CircularRevealCoordinatorLayout rootLayout = findViewById(R.id.editReminder);
        revealAnimation = new RevealAnimation(rootLayout, intent, this);

        // LiveData
        reminderLiveData = editReminderViewModel.getReminder(reminderId);
        reminderLiveData.observe(this, new Observer<Reminder>() {
            @Override
            public void onChanged(Reminder reminder) {
                titleEditText.setText(reminder.getTitle());
                timeEditText.setText(reminder.getTime());
                dateEditText.setText(reminder.getDate());
                descEditText.setText(reminder.getDescription());
                alarmId = reminder.getAlarmId();
//                alarmIdFromData = reminder.getAlarmId();
                Log.d("ALARMID LIVEDATA", String.valueOf(alarmId));
                if (alarmId != 0) {
                    materialCheckBox.setChecked(true);
                    notificationRequested = true;
                } else {
                    materialCheckBox.setChecked(false);
                    timeDate.setVisibility(View.GONE);
                    notificationRequested = false;
                }

                Log.d("CHECKBOX AT OBSERVER", String.valueOf(materialCheckBox.isChecked()));

                Log.d("DATE EDIT TEXT", timeEditText.getText().toString());

            }

        });

        Log.d("CHECKBOX After OBSERVER", String.valueOf(materialCheckBox.isChecked()));

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

        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CHECKBOX AT LISTENER", String.valueOf(materialCheckBox.isChecked()));
                if (isChecked) {
                    timeDate.setVisibility(View.VISIBLE);
                    notificationRequested = true;
                } else {
                    timeDate.setVisibility(View.GONE);
                    notificationRequested = false;
                }
            }
        });

    }

    public static boolean isTitleValid(@Nullable CharSequence text) {
        return text != null && text.length() >= 1;
    }

    public void setReminderInfo() {
        Log.d("Notification Requested at Edit Reminder Set", String.valueOf(notificationRequested));

        if (notificationRequested) {
            alarmId = (int) System.currentTimeMillis();
//
        } else {
            alarmId = 0;
            timeEditText.setText("");
            dateEditText.setText("");
        }

        Log.d("DATE EDIT TEXT", timeEditText.getText().toString());
        Reminder editedReminder = new Reminder(
                reminderId,
                Objects.requireNonNull(titleEditText.getText()).toString(),
                timeEditText.getText().toString(),
                Objects.requireNonNull(dateEditText.getText()).toString(),
                descEditText.getText().toString(),
                alarmId
        );
        Log.d("Set Reminder AlarmId", String.valueOf(alarmId));
        reminderViewModel.update(editedReminder);


        Toast.makeText(
                getApplicationContext(),
                "Reminder Updated",
                Toast.LENGTH_LONG).show();
    }

    // Time Selection Listener
    public void timeListener() {
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime();

            }
        });
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timeEditText.setShowSoftInputOnFocus(false);
                }
            }
        });
    }

    // Time setup
    public void setUpTime() {
        //                R.style.RemindMe_Picker,
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
//                R.style.RemindMe_Picker,
                EditReminder.this,
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
//        timeSet = time;
        timeString = timeFormatter.format(time);
        timeEditText.setText(timeString);
//        timeEditText.setShowSoftInputOnFocus(false);
    }

    // Date Selection Listener
    public void dateListener() {
//        final TextInputEditText setDate = findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDate();
            }
        });
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateEditText.setShowSoftInputOnFocus(false);
                }
            }
        });
    }

    // Date setup
    public void setUpDate() {
        //                R.style.RemindMe_Picker,
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
//                R.style.RemindMe_Picker,
                EditReminder.this,
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
//        final TextInputEditText dateEditText = findViewById(R.id.dateEditText);
        dateString = dateFormatter.format(date);
        dateEditText.setText(dateString);
    }

    private void startNotification() {

        AlarmHandler alarmHandler = new AlarmHandler(getApplication());
        Alarm alarm = new Alarm(
                Objects.requireNonNull(titleEditText.getText()).toString(),
                Objects.requireNonNull(descEditText.getText()).toString(),
                alarmId,
                calendar.getTimeInMillis());

        alarmHandler.addAlarm(alarm);

//        Intent notifyIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//        PendingIntent notifyPendingIntent;
//        Log.d("title", String.valueOf(titleEditText.getText()));
//        notifyIntent.putExtra("title", String.valueOf(titleEditText.getText()));
//        notifyIntent.putExtra("description", String.valueOf(descEditText.getText()));
//
//        if (materialCheckBox.isChecked()) {
//            if (alarmId == 0) {
//                alarmId = calendar.getTimeInMillis();
//            }
//            notifyPendingIntent = PendingIntent.getBroadcast
//                    (getApplicationContext(), (int) alarmId, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifyPendingIntent);
//                Toast.makeText(this, "Event scheduled at "
//                                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
//                                + calendar.get(Calendar.MINUTE) + " "
//                                + calendar.get(Calendar.DAY_OF_MONTH) + "/"
//                                + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR),
//                        Toast.LENGTH_LONG).show();
//            }
//        } else {
//            notifyPendingIntent = PendingIntent.getBroadcast
//                    (getApplicationContext(), (int) alarmId, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            alarmManager.cancel(notifyPendingIntent);
//        }
    }
}
