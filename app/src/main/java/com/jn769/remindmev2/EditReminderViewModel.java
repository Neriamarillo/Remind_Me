package com.jn769.remindmev2;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

class EditReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;

    private final ReminderDao reminderDao;

    public EditReminderViewModel(Application application) {
        super(application);
        String TAG = this.getClass().getSimpleName();
        Log.i(TAG, "Edit ViewModel");
        ReminderDatabase db = ReminderDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
    }

    public LiveData<Reminder> getReminder(int id) {
        return reminderDao.getReminder(id);
    }


}
