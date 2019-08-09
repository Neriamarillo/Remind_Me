package com.jn769.remindmev2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class BootBroadcastReceiver extends BroadcastReceiver {

    AlarmHandler alarmHandler;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (Objects.requireNonNull(intent.getAction()).equals("android.intent.action.BOOT_COMPLETED")) {

            App application = (App) context.getApplicationContext();
            alarmHandler = new AlarmHandler(application);
            Log.d("BOOT COMPLETED: ", "INIT ALARM HANDLER");
            alarmHandler.recoverAlarms(context);
            Log.d("BOOT COMPLETED: ", "RECOVERED ALARMS");

        } else {
            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();

        }
    }
}
