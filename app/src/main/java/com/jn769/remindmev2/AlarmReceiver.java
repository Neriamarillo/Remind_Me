package com.jn769.remindmev2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "Reminders";
    private static String GROUP_KEY_REMINDERS = "GROUP_KEY_REMINDERS";
    int SUMMARY_ID = 0;

    private NotificationManager notificationManager;

    private List<Intent> intentList;
    private Alarm alarm;
    private List<Alarm> alarmList;

    private String title;
    private String description;
    private long notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {


//        Toast.makeText(context, "Boot Completed", Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Alarm Broadcast Received", Toast.LENGTH_LONG).show();
        Log.d("Alarm Broadcast Received", "RECEIVED");


//            Toast.makeText(context, "Notification Count: " + notificationId, Toast.LENGTH_LONG).show();

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        title = bundle.getString("title");
        description = bundle.getString("description");
        notificationId = bundle.getLong("alarmId");

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        deliverNotification(context);

    }

    private void deliverNotification(Context context) {

        Notification builder;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        boolean isBundledNotification = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Reminders")
                        //set content text to support devices running API level < 24
                        .setContentText("New Reminders")
                        .setSmallIcon(R.drawable.ic_beenhere_black_24dp)
                        .setColor(ContextCompat.getColor(context, R.color.secondaryColor))
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle())
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY_REMINDERS)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        if (description != null) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(Objects.requireNonNull(description.trim()))
                    .setSmallIcon(R.drawable.ic_beenhere_black_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.secondaryColor))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Objects.requireNonNull(description)))
                    .setGroup(GROUP_KEY_REMINDERS)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_beenhere_black_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.secondaryColor))
                    .setGroup(GROUP_KEY_REMINDERS)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
        }

        if (isBundledNotification) {
            notificationManager.notify(SUMMARY_ID, summaryNotification);
            notificationManager.notify((int) notificationId, builder);
        } else {
            notificationManager.notify((int) notificationId, builder);
        }

//        notificationManager.cancel(notificationId);
    }
}
