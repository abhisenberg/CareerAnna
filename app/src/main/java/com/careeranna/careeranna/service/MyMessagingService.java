package com.careeranna.careeranna.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.NotificationActivity;
import com.careeranna.careeranna.data.UrlConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MyMessagingService extends FirebaseMessagingService {

    public static final String NOTIF_LIST = "NotifyList";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            Paper.init(getApplicationContext());

            com.careeranna.careeranna.data.Notification currentNotif = new com.careeranna.careeranna.data.Notification(
                    remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"),
                    remoteMessage.getData().get("img_url"),
                    remoteMessage.getData().get("date"),
                    remoteMessage.getData().get("redirect_url"));
            showNotification(currentNotif);
            addNotificationToList(currentNotif);
    }


    private void showNotification(com.careeranna.careeranna.data.Notification currentNotif){

        Intent intent =  new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(currentNotif.getRedirectURl())
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, 0);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationCompat.Builder builder;
            Intent intent1 =new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(currentNotif.getRedirectURl())
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent1;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", "CareerAnna", importance);
                mChannel.setDescription(currentNotif.getDescription());
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent1 = PendingIntent.getActivity(this, 1251, intent1, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.ic_stat_name) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent1)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(currentNotif.getDescription()))
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION));
                   Notification notification = builder.build();
                    notifManager.notify(0, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setChannelId("CareerAnna")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(currentNotif.getDescription()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(999, notification);
        }
    }

    private void addNotificationToList (com.careeranna.careeranna.data.Notification currentNotif){
        ArrayList<com.careeranna.careeranna.data.Notification> notifList =
                Paper.book().read(NOTIF_LIST, new ArrayList<com.careeranna.careeranna.data.Notification>());

        notifList.add(currentNotif);

        Paper.book().write(NOTIF_LIST, notifList);
    }

}
