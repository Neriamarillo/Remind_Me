package com.jn769.remindmev2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;
    private final LiveData<List<Reminder>> reminderList;

    public ReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
        reminderList = reminderRepository.getReminderList();
    }

    LiveData<List<Reminder>> getReminderList() {
        return reminderList;
    }

    public void insert(Reminder reminder) {
        reminderRepository.insert(reminder);
    }

    public void deleteReminder(final int position) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                reminderRepository.delete(reminderList.getValue().get(position));
            }
        });
        thread.start();
    }

}
