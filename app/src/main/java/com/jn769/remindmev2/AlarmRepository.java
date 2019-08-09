package com.jn769.remindmev2;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmRepository {

    private AlarmDao alarmDao;
//    private List<Alarm> alarmList;

    AlarmRepository(Application application) {
        ReminderDatabase db = ReminderDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
//        alarmList = alarmDao.getAlarms();
    }

//    List<Alarm> getAlarmList() {
//        return alarmList;
//    }

    public void insert(Alarm alarm) {
        new AlarmRepository.insertAsyncTask(alarmDao).execute(alarm);
    }

    public void delete(Alarm alarm) {
        new AlarmRepository.deleteAsyncTask(alarmDao).execute(alarm);
    }

    public List<Alarm> getAlarmList() throws ExecutionException, InterruptedException {
        return new getAsyncTask(alarmDao).execute().get();
    }

    private static class insertAsyncTask extends AsyncTask<Alarm, Void, Void> {

        private AlarmDao mAsyncTaskDao;

        insertAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.addAlarm(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Alarm, Void, Void> {

        private AlarmDao mAsyncTaskDao;

        deleteAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.deleteAlarm(params[0]);
            return null;
        }

    }

    private static class getAsyncTask extends AsyncTask<Alarm, Void, List<Alarm>> {

        private AlarmDao mAsyncTaskDao;

        getAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Alarm> doInBackground(Alarm... alarms) {
            return mAsyncTaskDao.getAlarms();
        }

    }
}
