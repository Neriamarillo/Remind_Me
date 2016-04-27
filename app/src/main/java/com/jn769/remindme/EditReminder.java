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

    public Reminder reminder;
    public RecyclerView.Adapter adapter;
    private final DatabaseHandler db = new DatabaseHandler(this);
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

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
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker = new TimePickerDialog(EditReminder.this, new TimePickerDialog.OnTimeSetListener() {
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

                DatePickerDialog mDatePicker = new DatePickerDialog(EditReminder.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        mcurrentDate.set(selectedYear, selectedMonth, selectedDay);
                        setDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });
    }

}
