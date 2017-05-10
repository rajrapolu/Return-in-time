package com.android.raj.returnintime.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        manager.notify(intent.getIntExtra(NOTIFICATION_ID, 1), notification);
    }
}
