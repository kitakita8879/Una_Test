package com.example.una_test;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TOPIC_ANDROID = "Android";
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String SHARED_PREF_FCM = "SHARED_PREF_FCM";
    private static final String FCM_TOKEN = "FCM_TOKEN";
    private static final String NORMAL_CHANNEL = "NORMAL_CHANNEL";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: token " + token);
        saveToken(token);
    }

    private void saveToken(@NonNull String token) {
        //save token ? 方便之後對特定設備發送訊息
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_FCM, MODE_PRIVATE);
        sharedPref.edit().putString(FCM_TOKEN, token).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            buildNotification(message.getNotification());
        }
        Log.d(TAG, "onMessageReceived: message " +
                Objects.requireNonNull(message.getNotification()).getTitle());
    }

    private void buildNotification(@NonNull RemoteMessage.Notification message) {
        Log.d(TAG, "buildNotification: start");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (message.getImageUrl() != null) {
            // TODO: 2024/8/16 開著app 時的通知的圖片未完成
            Log.d(TAG, "buildNotification: image uri " + message.getImageUrl());
            try (InputStream inputStream = this.getContentResolver().openInputStream(message.getImageUrl())) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap));
            } catch (IOException e) {
                Log.e(TAG, "buildNotification: image get fail");
            }
        }

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelDesc = "desc";
            NotificationChannel channel = new NotificationChannel(NORMAL_CHANNEL, NORMAL_CHANNEL,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDesc);
            notificationManager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "buildNotification: notification permission deny");
            return;
        }
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }
}