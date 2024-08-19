package com.example.una_test;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
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

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class TestRotateScreenActivity extends AppCompatActivity {
    private int mAnswer;
    private boolean mIsConnect;
    private View mViewConnectSimulation;
    private TextView mTxtAnswer;
    private static final String CHANNEL_ID = "TEST_CHANNEL_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rotate_screen);

        mAnswer = 0;
        mIsConnect = false;
        initView();
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
                        Toast.makeText(TestRotateScreenActivity.this,
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

        findViewById(R.id.txt_1).setOnClickListener(v -> callNotify());

        findViewById(R.id.txt_2).setOnClickListener(v ->
                registerTopic(MyFirebaseMessagingService.TOPIC_ANDROID));

        findViewById(R.id.txt_3).setOnClickListener(v ->
                FirebaseMessaging.getInstance()
                        .unsubscribeFromTopic(MyFirebaseMessagingService.TOPIC_ANDROID));
    }

    private void registerTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    String result = "fail";
                    if (task.isSuccessful()) {
                        result = "success";
                    }
                    Log.e("UNA", "onComplete: subscribe topic " + topic + result);
                });
    }

    private void callNotify() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.info)
                .setContentTitle("TEST")
                .setContentText("TEST TEST")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String channelName = "name";
        String channelDesc = "desc";
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDesc);
            notificationManager.createNotificationChannel(channel);
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "no notification permission", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 不會執行onPause onStop
        //onSaveInstance 因api 差異可能會執行onStop 階段
        setContentView(R.layout.activity_test_rotate_screen);
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
