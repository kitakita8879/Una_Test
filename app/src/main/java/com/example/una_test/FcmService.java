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
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class FcmService extends FirebaseMessagingService {
    public static final String SHARED_PREF_FCM = "SHARED_PREF_FCM";
    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String FCM_TOPICS = "FCM_TOPICS";
    private static final String FCM_COUNTRY = "FCM_COUNTRY";
    public static final String TOPIC_ANDROID = "Android";
    private static final String TAG = "MyFirebaseMessagingService";
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
            if (!message.getData().isEmpty()) {
                if (message.getData().containsKey("web")) {
                    String data = message.getData().get("web");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    buildNotification(message.getNotification(), pendingIntent);
                } else {
                    buildNotification(message.getNotification(), null);
                }
            } else {
                buildNotification(message.getNotification(), null);
            }
        }
    }

    public enum FcmTopic {
        TOPIC_A("TopicA");

        private final String mTopicName;

        FcmTopic(@NonNull String topicName) {
            this.mTopicName = topicName;
        }

        public String getTopic(String country) {
            String topic;
            if (country.equalsIgnoreCase("TW")) {
                topic = "Tw" + mTopicName;
            } else {
                topic = mTopicName;
            }
            return topic;
        }
    }

    public static void registerTopic(FcmTopic fcmTopic, Context context) {
        new Thread(() -> {
            SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_FCM, MODE_PRIVATE);
            String topic = fcmTopic.getTopic(getCountry(context));
            Set<String> topics = sharedPref.getStringSet(FCM_TOPICS, new HashSet<>());
            if (topics.contains(fcmTopic.toString())) return;
            HashSet<String> finalTopics = new HashSet<>(topics);
            finalTopics.add(topic);
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sharedPref.edit().putStringSet(FCM_TOPICS, finalTopics).apply();
                        }
                        Log.d(TAG, "registerTopic: " + topic +
                                (task.isSuccessful() ? " success" : " fail"));
                    });
        }).start();
    }

    public static void saveCountry(String country, Context context) {
        new Thread(() -> {
            SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_FCM, MODE_PRIVATE);
            sharedPref.edit().putString(FCM_COUNTRY, country).apply();
        }).start();
    }

    private static String getCountry(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_FCM, MODE_PRIVATE);
        return sharedPref.getString(FCM_COUNTRY, "US");
    }

    private void buildNotification(@NonNull RemoteMessage.Notification message, @Nullable PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_chargermode)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (message.getImageUrl() != null) {
            try {
                URL url = new URL(message.getImageUrl().toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                try (InputStream inputStream = connection.getInputStream()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    builder.setLargeIcon(bitmap)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap)
                                    .bigLargeIcon(null));
                }
            } catch (IOException e) {
                Log.e(TAG, "buildNotification: get image fail " + e.getMessage());
            }
        }

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelDesc = "desc";
            NotificationChannel channel = notificationManager.getNotificationChannel(NORMAL_CHANNEL);
            if (channel == null) {
                channel = new NotificationChannel(NORMAL_CHANNEL, NORMAL_CHANNEL,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(channelDesc);
                notificationManager.createNotificationChannel(channel);
            }
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
