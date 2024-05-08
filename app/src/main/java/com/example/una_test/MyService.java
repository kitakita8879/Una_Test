package com.example.una_test;

import static com.example.una_test.TestComponentsActivity.TAG_SERVICE;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    @Override
    public void onCreate() {
        Log.d(TAG_SERVICE, "service onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG_SERVICE, "service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG_SERVICE, "service onDestroy");
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public void showLog() {
            Log.d(TAG_SERVICE, "MyBinder showLog");
        }

        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG_SERVICE, "service onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG_SERVICE, "service onUnbind");
        return super.onUnbind(intent);
    }
}