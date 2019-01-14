package com.careeranna.careeranna.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("Title"), remoteMessage.getData().get("Body"));
    }

    public void showNotification(String title, String message) {

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_expanded_layout);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.text, message);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.red))
                .setChannelId("CareerAnna")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, notification);

/*
        Intent cancelIntent = new Intent(this, MainActivity.class);
        PendingIntent content = PendingIntent.getActivity(this,
                0, cancelIntent, 0);


        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setWhen(0)
                .addAction(R.drawable.close, "Cancel", content)
                .setContentText(message)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
*/

    }
}
