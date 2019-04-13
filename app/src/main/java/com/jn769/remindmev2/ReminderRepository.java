package com.jn769.remindmev2;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> reminderList;

    ReminderRepository(Application application) {
        ReminderDatabase db = ReminderDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
        reminderList = reminderDao.getAllReminders();
    }

    LiveData<List<Reminder>> getReminderList() {
        return reminderList;
    }

    public void insert(Reminder reminder) {
        new insertAsyncTask(reminderDao).execute(reminder);
    }

    public void delete(Reminder reminder) {
        new deleteAsyncTask(reminderDao).execute(reminder);
    }

    private static class insertAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao mAsyncTaskDao;

        insertAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.addReminder(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao mAsyncTaskDao;

        deleteAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.deleteReminder(params[0]);
            return null;
        }

    }
}
