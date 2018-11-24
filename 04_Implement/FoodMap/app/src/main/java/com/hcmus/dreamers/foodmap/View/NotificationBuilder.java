package com.hcmus.dreamers.foodmap.View;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.hcmus.dreamers.foodmap.MainActivity;
import com.hcmus.dreamers.foodmap.R;

public class NotificationBuilder {
    public static void ShowNotification(Context context, String title, String content){
        NotificationManager mNotificationManager =
                (NotificationManager) ((AppCompatActivity)context).getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("foodmapnotify",
                    "FoodMap",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(content);
            mNotificationManager.createNotificationChannel(channel);
        }

        Uri soundNotify = Uri.parse("android.resource://com.hcmus.dreamers.foodmap/" + R.raw.messenger_sound);;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "foodmapnotify")
                .setSmallIcon(R.drawable.ic_notifications) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setSound(soundNotify) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
