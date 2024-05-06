package com.example.una_test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestComponentsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_components);

        Intent intent = new Intent(TestComponentsActivity.this, MyService.class);
        findViewById(R.id.btn_start).setOnClickListener(v -> startService(intent));
        findViewById(R.id.btn_stop).setOnClickListener(v -> stopService(intent));

        findViewById(R.id.btn_bind).setOnClickListener(v -> bindService(intent, connection,
                BIND_AUTO_CREATE));

        findViewById(R.id.btn_unbind).setOnClickListener(v -> unbindService(connection));
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder mBinder = (MyService.MyBinder) service;
            mBinder.showLog();
            Log.d("MyService", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MyService", "onServiceDisconnected");
        }
    };
}
