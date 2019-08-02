package com.jn769.remindmev2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> reminderList;
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

    private static class updateAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao mAsyncTaskDao;

        updateAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.updateReminder(params[0]);
            return null;
        }
    }

//    private static class getAsyncTask extends AsyncTask<Reminder, Void, Reminder> {
//
//        private ReminderDao mAsyncTaskDao;
//
//        getAsyncTask(ReminderDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Reminder doInBackground(Reminder... reminders) {
//            return null;
//        }


//        protected Reminder doInBackground(final int position) {
//            mAsyncTaskDao.getReminder(position);
//            return null;
//        }

//        @Override
//        protected Reminder doInBackground(final Reminder... params) {
//            return mAsyncTaskDao.getReminder(params);
////            return null;
//        }
//    }

}
