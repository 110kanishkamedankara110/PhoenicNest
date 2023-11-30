package com.phoenix.phoenixNest.util;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.phoenix.phoenicnest.R;

public class Notifications {
    static NotificationManager notificationManager;

    public static void registerNotificationChannel(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence name = activity.getString(R.string.channelName);
                String description = activity.getString(R.string.channelDescription);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
                channel.setDescription(description);

                notificationManager = activity.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

    }
}
