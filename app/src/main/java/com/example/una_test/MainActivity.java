package com.example.una_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.una_test.HBCameraTest.HBCameraTestActivity;
import com.example.una_test.ssCamera.SsCameraActivity;
import com.example.una_test.xmCamera.XmCameraActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_1).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    ListViewAndRecycleViewActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_2).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    ChargingMainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_3).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestLedMainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_4).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestComponentsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_5).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestFragmentActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_6).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestTryCatchActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_7).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestThreadActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_8).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    AbstractFactoryPatternActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_9).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    EditTextActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_10).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestStreamActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_11).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestRotateAndNotificationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_12).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    HBCameraTestActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_13).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    XmCameraActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_14).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    SsCameraActivity.class);
            startActivity(intent);
        });

        checkNotificationPermission();

        String notificationData = getIntent().getStringExtra("web");
        if (notificationData != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notificationData));
            startActivity(intent);
        }
        getCountry();
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private void getCountry() {
        String country = Locale.getDefault().getCountry();
        FcmService.saveCountry(country, this);
    }
}