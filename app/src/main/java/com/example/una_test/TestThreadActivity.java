package com.example.una_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestThreadActivity extends AppCompatActivity {

    private final String TAG = "THREAD_PRACTISE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread);

        TextView txtHandler = findViewById(R.id.txt_handler);

        findViewById(R.id.btn_thread).setOnClickListener(v -> {
            new MyThread().start();
            MyRunnable myRunnable = new MyRunnable(5);
            new Thread(myRunnable, "runnable 1 ").start();
            new Thread(myRunnable, "runnable 2 ").start();
        });

        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        txtHandler.setText(msg.obj.toString());
                        txtHandler.setBackgroundColor(getColor(R.color.transparent));
                        break;
                    case 2:
                        txtHandler.setText(msg.obj.toString());
                        txtHandler.setBackgroundColor(getColor(R.color.blue1));
                        break;
                }
            }
        };
        Handler handler2 = new Handler(getMainLooper());

        findViewById(R.id.btn_handler1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = "線程1的東東們";
                        handler.sendMessage(msg);
                        handler2.post(() -> txtHandler.setText(String.format("%s post 1",
                                txtHandler.getText())));
                    }
                }.start();
            }
        });
        findViewById(R.id.btn_handler2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Message msg = Message.obtain();
                        msg.what = 2;
                        msg.obj = "線程2的咪咪咪咪";
                        handler.sendMessage(msg);
                        handler2.post(() -> txtHandler.setText(String.format("%s post 22",
                                txtHandler.getText())));
                    }
                }.start();
            }
        });

        findViewById(R.id.btn_run_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        runOnUiThread(() -> txtHandler.setText(R.string.label_run_on_ui));
                        txtHandler.post(() -> txtHandler.setBackgroundColor(getColor(R.color.black2)));
                    }
                }.start();
            }
        });
    }


    private class MyRunnable implements Runnable {

        private int mNum;

        MyRunnable(int num) {
            this.mNum = num;
        }

        @Override
        public void run() {
            while (mNum > 0) {
                synchronized (this) {
                    Log.d(TAG, Thread.currentThread().getName() + " number " + mNum--);
                }
            }
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 5; i >= 0; i--) {
                Log.d(TAG, getName() + " number: " + i);
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
