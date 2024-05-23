package com.example.una_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class TestThreadActivity extends AppCompatActivity {

    private final String TAG = "THREAD_PRACTISE";
    private TextView txtAns, txtAns2, txtTime, txtTime2;
    private long startTime;
    private final HashSet<Integer> mNodeSet = new HashSet<>();
    private final List<Integer> mIntegerList = new ArrayList<>();
    private final List<Integer> mIntegerList2 = new ArrayList<>();
    private final List<Integer> output = new ArrayList<>();
    private final List<Integer> output2 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread);

        TextView txtHandler = findViewById(R.id.txt_handler);
        EditText editNode = findViewById(R.id.edit_node);
        txtAns = findViewById(R.id.txt_ans);
        txtAns2 = findViewById(R.id.txt_ans2);
        txtTime = findViewById(R.id.txt_time);
        txtTime2 = findViewById(R.id.txt_time2);

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

        findViewById(R.id.btn_input).setOnClickListener(v -> {
            int start = checkNum(editNode);
            if (start != 0) {
                searchGraph(start);
                searchGraphChoco(start);
                startTime = System.currentTimeMillis();
            }
        });

        findViewById(R.id.btn_block).setOnClickListener(v -> {
            new Thread(this::threadBlockChoco).start();
            new Thread(this::threadBlock).start();
        });
    }

    private void threadBlockChoco() {
        mIntegerList2.clear();
        long funStart = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    Thread.sleep(index * 1000);
                    mIntegerList2.add(index);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException ignore) {
        }
        runOnUiThread(() -> {
            long duration = System.currentTimeMillis() - funStart;
            txtTime2.setText(String.format("%s %s ms", mIntegerList2, duration));
        });
    }

    private void threadBlock() {
        mIntegerList.clear();
        long funStart = System.currentTimeMillis();
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            int index = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(index * 1000);
                    mIntegerList.add(index);
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < 3; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        runOnUiThread(() -> {
            long duration = System.currentTimeMillis() - funStart;
            txtTime.setText(String.format("%s %s ms", mIntegerList, duration));
        });
    }

    private int checkNum(EditText editNum) {
        try {
            int num = Integer.parseInt(editNum.getText().toString());
            if (num == 0 || num > 7) {
                throw new NumberFormatException();
            }
            return num;
        } catch (NumberFormatException e) {
            Log.d(TAG, e.toString());
            txtAns.setText(R.string.label_please_input_17_sign);
            txtAns2.setText(R.string.label_please_input_17_sign);
            return 0;
        }
    }

    @NonNull
    private List<Integer> fetchNeighbours(int node) {
        try {
            // 模擬網路請求延遲
            Thread.sleep((int) (Math.random() * 10) * 100L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switch (node) {
            case 1:
                return Arrays.asList(2, 3, 4);
            case 2:
                return Arrays.asList(1, 5);
            case 3:
                return Arrays.asList(1, 5);
            case 4:
                return Arrays.asList(1, 6);
            case 5:
                return Arrays.asList(2, 3, 7);
            case 6:
                return Arrays.asList(4, 7);
            case 7:
                return Arrays.asList(5, 6);
            default:
                throw new IllegalArgumentException("node " + node + " not define");
        }
    }

    private void searchGraph(int start) {
        output.clear();
        new Thread(() -> fetchShow(Collections.singletonList(start))).start();
    }

    private void fetchShow(List<Integer> fetchList) {
        List<Integer> tmp = new ArrayList<>();
        for (int j = 0; j < output.size(); j++) {
            for (int k = fetchList.size() - 1; k >= 0; k--) {
                if (output.get(j).equals(fetchList.get(k))) {
                    fetchList.remove(k);
                }
            }
        }
        fetchList = fetchList.stream().distinct().collect(Collectors.toList());
        Thread[] threads = new Thread[fetchList.size()];
        for (int i = 0; i < fetchList.size(); i++) {
            int index = fetchList.get(i);
            output.add(index);
            List<Integer> newOutput = new ArrayList<>(output);
            runOnUiThread(() -> {
                long currentTime = System.currentTimeMillis() - startTime;
                txtAns.setText(String.format("%s %s ms", newOutput, currentTime));
            });
            threads[i] = new Thread(() -> tmp.addAll(fetchNeighbours(index)));
            threads[i].start();
        }
        for (int i = 0; i < fetchList.size(); i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!tmp.isEmpty()) {
            fetchShow(tmp);
        }
    }

    private void searchGraphChoco(int start) {
        mNodeSet.clear();
        output2.clear();
        // 請在非 UI 線程執行
        new Thread(() -> printAndFetch(Collections.singletonList(start))).start();
        // 如果嚴格要求一行 print 所有內容就另外帶入 List, 把 Log 的 node 存入 List, 上面執行完再 print List
    }

    private void printAndFetch(@NonNull List<Integer> nodes) {
        ArrayList<Integer> nextNodes = new ArrayList<>();
        Thread[] threads = new Thread[nodes.size()];
        for (Integer node : nodes) {
            if (!mNodeSet.add(node)) {
                continue;
            }
            output2.add(node);
            List<Integer> newOutput = new ArrayList<>(output2);
            runOnUiThread(() -> {
                long currentTime = System.currentTimeMillis() - startTime;
                txtAns2.setText(String.format("%s %s ms", newOutput, currentTime));
            });
            threads[nodes.indexOf(node)] = new Thread(() -> nextNodes.addAll(fetchNeighbours(node)));
            threads[nodes.indexOf(node)].start();
        }
        for (int i = 0; i < nodes.size(); i++) {
            try {
                if (threads[i] != null)
                    threads[i].join();
            } catch (InterruptedException ignore) {
            }
        }
        if (!nextNodes.isEmpty()) {
            printAndFetch(nextNodes);
        }
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
