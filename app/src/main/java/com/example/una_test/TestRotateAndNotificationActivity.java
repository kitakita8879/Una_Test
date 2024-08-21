package com.example.una_test;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TestRotateAndNotificationActivity extends AppCompatActivity {
    private static final String CHANNEL_HIGH = "CHANNEL_HIGH";
    private static final String CHANNEL_DEFAULT = "CHANNEL_DEFAULT";
    private static final String CHANNEL_LOW = "CHANNEL_LOW";
    private static final String CHANNEL_MIN = "CHANNEL_MIN";
    private int mAnswer;
    private boolean mIsConnect;
    private View mViewConnectSimulation;
    private TextView mTxtAnswer;
    private SharedPreferences mSharedPref;
    private FirebaseAnalytics mAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rotate_and_notification);

        mAnswer = 0;
        mIsConnect = false;
        initView();
        mSharedPref = getSharedPreferences(MyFirebaseMessagingService.SHARED_PREF_FCM, MODE_PRIVATE);
        mAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //模擬連線
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mIsConnect) {
                    mIsConnect = true;
                    runOnUiThread(() -> {
                        mViewConnectSimulation.setBackgroundColor(getResources().getColor(
                                mIsConnect ? R.color.blue1 : R.color.red, getTheme()));
                        Toast.makeText(TestRotateAndNotificationActivity.this,
                                "onResume connect", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }, 2000);
    }

    private void initView() {
        mTxtAnswer = findViewById(R.id.txt_answer);
        mViewConnectSimulation = findViewById(R.id.v_5);

        mViewConnectSimulation.setBackgroundColor(getResources().getColor(
                mIsConnect ? R.color.blue1 : R.color.red, getTheme()));
        mTxtAnswer.setText(String.valueOf(mAnswer));

        findViewById(R.id.btn_add).setOnClickListener(v -> {
            mAnswer = mAnswer + 1;
            mTxtAnswer.setText(String.valueOf(mAnswer));
        });

        findViewById(R.id.btn_sub).setOnClickListener(v -> {
            if (mAnswer > 0) {
                mAnswer = mAnswer - 1;
                mTxtAnswer.setText(String.valueOf(mAnswer));
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());

        findViewById(R.id.txt_notification_default).setOnClickListener(v ->
                callNotify(CHANNEL_DEFAULT, R.string.label_notification_default));
        findViewById(R.id.txt_notification_high).setOnClickListener(v ->
                callNotify(CHANNEL_HIGH, R.string.label_notification_high));
        findViewById(R.id.txt_notification_low).setOnClickListener(v ->
                callNotify(CHANNEL_LOW, R.string.label_notification_low));
        findViewById(R.id.txt_notification_min).setOnClickListener(v ->
                callNotify(CHANNEL_MIN, R.string.label_notification_min));

        findViewById(R.id.txt_2).setOnClickListener(v ->
                registerTopic(MyFirebaseMessagingService.TOPIC_ANDROID));

        findViewById(R.id.txt_3).setOnClickListener(v ->
                unregisterTopic(MyFirebaseMessagingService.TOPIC_ANDROID));

        findViewById(R.id.txt_notification_setting).setOnClickListener(v -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_DEFAULT);
                startActivity(intent);
            }
        });

        findViewById(R.id.txt_enable_notify).setOnClickListener(v -> enableFMC());
        findViewById(R.id.txt_disable_notify).setOnClickListener(v -> disableFMC());
    }

    private void enableFMC() {
        new Thread(() -> {
            String token = mSharedPref.getString(MyFirebaseMessagingService.FCM_TOKEN, "");
            if (token.isEmpty()) {
                FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    String result = "fail";
                    if (task.isSuccessful()) {
                        result = "success";
                    }
                    Log.e("UNA", "enableFMC: " + result);
                });
            }
        }).start();
    }

    private void disableFMC() {
        new Thread(() -> {
            Set<String> topics = mSharedPref.getStringSet(MyFirebaseMessagingService.FCM_TOPICS,
                    new HashSet<>());
            if (!topics.isEmpty()) {
                for (String topic : topics) {
                    unregisterTopic(topic);
                }
            }
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
                String result = "fail";
                if (task.isSuccessful()) {
                    FirebaseMessaging.getInstance().setAutoInitEnabled(false);
                    mSharedPref.edit().putString(MyFirebaseMessagingService.FCM_TOKEN, "").apply();
                    result = "success";
                }
                Log.e("UNA", "disableFMC: " + result);
            });
        }).start();
    }

    private void registerTopic(String topic) {
        new Thread(() -> {
            Set<String> topics = mSharedPref.getStringSet(MyFirebaseMessagingService.FCM_TOPICS,
                    new HashSet<>());
            if (topics.contains(topic)) return;
            HashSet<String> finalTopics = new HashSet<>(topics);
            finalTopics.add(topic);
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mSharedPref.edit().putStringSet(
                                    MyFirebaseMessagingService.FCM_TOPICS, finalTopics).apply();
                            Log.e("UNA", "registerTopic: " + finalTopics);
                        } else {
                            Log.e("UNA", "registerTopic: fail");
                        }
                    });
            mAnalytics.setUserProperty(topic, "true");
        }).start();
    }

    private void unregisterTopic(String topic) {
        new Thread(() -> {
            Set<String> topics = mSharedPref.getStringSet(MyFirebaseMessagingService.FCM_TOPICS,
                    new HashSet<>());
            if (!topics.contains(topic)) return;
            HashSet<String> finalTopics = new HashSet<>(topics);
            finalTopics.remove(topic);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mSharedPref.edit().putStringSet(
                                    MyFirebaseMessagingService.FCM_TOPICS, finalTopics).apply();
                            Log.e("UNA", "unregisterTopic: " + topics +
                                    "\nnow register topics " + finalTopics);
                        } else {
                            Log.e("UNA", "unregisterTopic: fail");
                        }
                    });
            mAnalytics.setUserProperty(topic, "false");
        }).start();
    }

    private void callNotify(String channelId, int title) {
        int priority, importance, notificationId;
        switch (channelId) {
            case CHANNEL_HIGH:
                priority = NotificationCompat.PRIORITY_HIGH;
                importance = NotificationManager.IMPORTANCE_HIGH;
                notificationId = 3;
                break;
            case CHANNEL_LOW:
                priority = NotificationCompat.PRIORITY_LOW;
                importance = NotificationManager.IMPORTANCE_LOW;
                notificationId = 1;
                break;
            case CHANNEL_MIN:
                priority = NotificationCompat.PRIORITY_MIN;
                importance = NotificationManager.IMPORTANCE_MIN;
                notificationId = 0;
                break;
            default:
                priority = NotificationCompat.PRIORITY_DEFAULT;
                importance = NotificationManager.IMPORTANCE_DEFAULT;
                notificationId = 2;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_chargermode)
                .setContentTitle(getResources().getText(title))
                .setContentText("TEST TEST")
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String channelDesc = channelId + " Priority " + priority;
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                channel = new NotificationChannel(channelId, channelId,
                        importance);
                channel.setDescription(channelDesc);
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("UNA", "callNotify: no notification permission ");
            return;
        }
        NotificationManagerCompat.from(this).notify(notificationId, builder.build());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 不會執行onPause onStop
        //onSaveInstance 因api 差異可能會執行onStop 階段
        setContentView(R.layout.activity_test_rotate_and_notification);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //模擬斷線
        mIsConnect = false;
        mViewConnectSimulation.setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
        Toast.makeText(this, "onStop disconnect", Toast.LENGTH_SHORT).show();
    }
}
