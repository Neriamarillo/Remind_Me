package com.jn769.remindmev2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class ReminderRepository {

    private final ReminderDao reminderDao;
    private final LiveData<List<Reminder>> reminderList;
    private Reminder singleReminder;

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

    public void update(Reminder reminder) {
        new updateAsyncTask(reminderDao).execute(reminder);
    }

    public LiveData<Reminder> getReminder(int id) {
        return reminderDao.getReminder(id);
    }

    public List<Reminder> getOffReminderList() {
        List<Reminder> offReminderList = reminderDao.getReminderList();
        return offReminderList;
    }

    public List<Reminder> getReminderAlarmId(long alarmId) {
        return reminderDao.getReminderAlarmId(alarmId);
    }

    private static class insertAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private final ReminderDao mAsyncTaskDao;

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

        private final ReminderDao mAsyncTaskDao;

        deleteAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.deleteReminder(params[0]);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private final ReminderDao mAsyncTaskDao;

        updateAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.updateReminder(params[0]);
            return null;
        }
    }

}
