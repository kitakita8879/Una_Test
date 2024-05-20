package com.example.una_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}