package com.example.una_test;

import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TestComponentsActivity extends AppCompatActivity {
    public static final String TAG_SERVICE = "MyService";
    private static final String TAG_RECEIVER = "MyReceiver";
    private static final String TEST_BROADCAST = "TEST_BROADCAST";
    private static final String TEST_ACTION = "com.example.una_test.MY_DYNAMIC_RECEIVE";
    private MyService mMyService;
    private IntentFilter mIntentFilter, mLocalIntentFilter;
    private LocalBroadcastManager mBroadcastManager;

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

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(TEST_ACTION);

        findViewById(R.id.btn_broadcast).setOnClickListener(v -> {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(TEST_ACTION);
            broadcastIntent.setPackage(getPackageName());
            broadcastIntent.putExtra(TEST_BROADCAST, "Dynamic Broadcast data 8888");
            sendBroadcast(broadcastIntent);
            Log.d(TAG_RECEIVER, "broadcast button onClick");
        });

        mLocalIntentFilter = new IntentFilter();
        mLocalIntentFilter.addAction(TEST_ACTION);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        findViewById(R.id.btn_local_broadcast).setOnClickListener(v -> {
            Intent localIntent = new Intent();
            localIntent.setAction(TEST_ACTION);
            localIntent.putExtra(TEST_BROADCAST, "Local Broadcast data 1111");
            mBroadcastManager.sendBroadcast(localIntent);
        });
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mDynamicReceiver, mIntentFilter, RECEIVER_NOT_EXPORTED);
            Log.d(TAG_RECEIVER, "android api 33 up register Receiver");
        } else {
            registerReceiver(mDynamicReceiver, mIntentFilter);
            Log.d(TAG_RECEIVER, "register Receiver");
        }
        mBroadcastManager.registerReceiver(mDynamicReceiver, mLocalIntentFilter);
        Log.d(TAG_RECEIVER, "register local Receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDynamicReceiver);
        Log.d(TAG_RECEIVER, "unregister Receiver");
        mBroadcastManager.unregisterReceiver(mDynamicReceiver);
        Log.d(TAG_RECEIVER, "unregister local Receiver");
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
            Log.d(TAG_RECEIVER, "onReceive: " + msg);
        }
    };
}