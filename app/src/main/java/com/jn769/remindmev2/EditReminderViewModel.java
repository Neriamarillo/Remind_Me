package com.jn769.remindmev2;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EditReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;

    private String TAG = this.getClass().getSimpleName();
    private ReminderDao reminderDao;
    private ReminderDatabase db;

    public EditReminderViewModel(Application application) {
        super(application);
        Log.i(TAG, "Edit ViewModel");
        db = ReminderDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
    }

    public LiveData<Reminder> getReminder(int id) {
        return reminderDao.getReminder(id);
    }


}
