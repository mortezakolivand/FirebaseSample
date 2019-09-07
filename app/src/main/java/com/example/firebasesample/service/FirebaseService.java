package com.example.firebasesample.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.firebasesample.MainActivity;
import com.example.firebasesample.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class FirebaseService extends FirebaseMessagingService {

    String TAG = "**";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
       // Log.d(TAG, "From: " + remoteMessage.getFrom());

        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications_active_24dp)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

        

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //showNotif(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
           // sendNotification(remoteMessage.getNotification().getBody());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



    private void showNotif(String body, String title) {

        String NOTIFICATION_CHANEL_ID = "com.example.firebasesample.test";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.i(TAG, "showNotif: "+Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANEL_ID,"notif" , NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("MK");
            notificationChannel.enableVibration(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);

        }



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , NOTIFICATION_CHANEL_ID);
        builder.setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL).
                setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.ic_notifications_active_24dp).
                setContentTitle(title).
                setContentText(body).
                setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(),builder.build());

    }

    private void sendNotification(String message) {
        try {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent in = new Intent(this, MainActivity.class);
            in.putExtra("notification", message);
            PendingIntent pi;
            pi = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_ONE_SHOT);
            initChannels(this);
            Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            androidx.core.app.NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon( R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setContentIntent(pi).setSound(sound);
            if (nm == null)return;
            nm.notify(0, noBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Sample@Channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Sample@Notification");
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.i(TAG, "onNewToken: Token Firebase :"+  s);

    }
}
