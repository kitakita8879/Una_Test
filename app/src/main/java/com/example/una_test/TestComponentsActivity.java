package com.example.una_test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestComponentsActivity extends AppCompatActivity {
    private Button btnBroadcast;
    private IntentFilter mIntentFilter;

    public static final String TAG_SERVICE = "MyService";
    private static final String TEST_BROADCAST = "TEST_BROADCAST";
    private static final String TEST_ACTION = "com.example.una_test.MyDynamicReceiver";
    private MyService mMyService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_components);

        Intent intent = new Intent(this, MyService.class);
        findViewById(R.id.btn_start).setOnClickListener(v -> startService(intent));
        findViewById(R.id.btn_stop).setOnClickListener(v -> stopService(intent));

        findViewById(R.id.btn_bind).setOnClickListener(v -> bindService(intent, mConnection,
                BIND_AUTO_CREATE));
        findViewById(R.id.btn_unbind).setOnClickListener(v -> {
            if (mMyService != null) {
                mMyService = null;
                unbindService(mConnection);
            }
        });

        btnBroadcast = findViewById(R.id.btn_broadcast);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(TEST_ACTION);

        btnBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(TEST_ACTION);
                intent.putExtra(TEST_BROADCAST, "Dynamic Broadcast data 8888");
                sendBroadcast(intent);
                Log.d("MyDynamicReceiver", "broadcast button onClick");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mDynamicReceiver, mIntentFilter, RECEIVER_NOT_EXPORTED);
        }
        Log.d("MyDynamicReceiver", "register Receiver");
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder mBinder = (MyService.MyBinder) service;
            mBinder.showLog();
            mMyService = mBinder.getService();
            Log.d(TAG_SERVICE, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG_SERVICE, "onServiceDisconnected");
            mMyService = null;
        }
    };

    private final BroadcastReceiver mDynamicReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(TEST_BROADCAST);
            Log.d("MyDynamicReceiver", "onReceive: " + msg);
        }
    };
}
