package com.jn769.remindmev2;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;

class AlarmHandler {

    Intent notifyIntent;
    private String title;
    private String description;
    private long notificationId;
    private long alarmId;
    private long storedAlarmId;

    private Alarm alarm;

    private Application app;

    private Context context;

    private List<Alarm> alarmList = new ArrayList<>();

    private AlarmRepository alarmRepository;

    AlarmHandler(Application application) {
        alarmRepository = new AlarmRepository(application);
        this.context = application.getBaseContext();
    }


    void addAlarm(Alarm alarm) {
        alarmRepository.insert(alarm);
        startNotification(context, alarm);
    }

    void deleteAlarm(int alarmId) {
        alarmRepository.delete(alarmList.get(alarmId));
    }


    List<Alarm> getAlarms() {
        return alarmList;
    }


    private void startNotification(Context context, Alarm alarm) {

        Log.d("START NOTIFICATION: ", "STARTED");

        try {
            alarmList = alarmRepository.getAlarmList();
            Log.d("START NOTIFICATION: ", "GOT ALARM LIST");
            Log.d("START NOTIFICATION: ALARM LIST SIZE", String.valueOf(alarmList.size()));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent notifyIntent = new Intent(context, AlarmReceiver.class);
        notifyIntent.putExtra("title", alarm.getTitle());
        notifyIntent.putExtra("description", alarm.getDescription());
        notifyIntent.putExtra("alarmId", alarm.getAlarmId());
        Log.d("START NOTIFICATION: ", alarm.getTitle());
        Log.d("START NOTIFICATION: ", alarm.getDescription());
        Log.d("START NOTIFICATION: ", String.valueOf(alarm.getAlarmId()));


        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("ALARM TIME: ", String.valueOf(alarm.getAlarmTime()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime(), notifyPendingIntent);
//            Toast.makeText(this, "Event scheduled at " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR), Toast.LENGTH_LONG).show();
            Log.d("START NOTIFICATION: ", "ALARM SET");

        } else {
            alarmManager.cancel(notifyPendingIntent);
        }
    }

    void recoverAlarms(Context context) {
        try {
            alarmList = alarmRepository.getAlarmList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("RECOVER ALARMS: ", "STARTED");
        Log.d("RECOVER ALARMS: ", String.valueOf(alarmList.size()));

        for (int i = 0; i <= alarmList.size() - 1; i++) {

            title = alarmList.get(i).getTitle();
            description = alarmList.get(i).getDescription();
            storedAlarmId = alarmList.get(i).getAlarmId();

            Intent notifyIntent = new Intent(context, AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            notifyIntent.putExtra("title", alarmList.get(i).getTitle());
            notifyIntent.putExtra("description", alarmList.get(i).getDescription());
            notifyIntent.putExtra("alarmId", alarmList.get(i).getAlarmId());

            PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                    (context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmList.get(i).getAlarmTime(), notifyPendingIntent);
            Log.d("RECOVER ALARMS: ", "RESCHEDULED");

//            }

        }
        Log.d("RECOVER ALARMS: ", "ALARMS RECOVERED");
    }


}
