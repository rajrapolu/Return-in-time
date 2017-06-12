package com.android.raj.returnintime.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.android.raj.returnintime.DetailActivity;
import com.android.raj.returnintime.MainActivity;
import com.android.raj.returnintime.R;

public class NotificationUtils {

    private static final String GROUP_KEY = "group_key";

    public static PendingIntent SetUpNotification(Context context, Uri uri, int NOTIFY_ID, String Title,
                                                  String returnTo, long calendar) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.setData(uri);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY)
                .setSmallIcon(R.drawable.ic_event_black_24dp)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(Title)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSubText(returnTo)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        return scheduleNotification(builder.build(), NOTIFY_ID, context, calendar);
    }

    private static PendingIntent scheduleNotification(Notification notification, int NOTIFY_ID,
                                             Context context, long calendar) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NOTIFY_ID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                NOTIFY_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar, pendingIntent);
        return pendingIntent;
    }

    public static void cancelNotification(Context context, int itemId) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                itemId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

    }

}
