package com.example.una_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtTest1 = findViewById(R.id.test_1);
        TextView txtTest2 = findViewById(R.id.test_2);
        TextView txtTest3 = findViewById(R.id.test_3);
        txtTest1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    ListViewAndRecycleViewActivity.class);
            startActivity(intent);
        });

        txtTest2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    ChargingMainActivity.class);
            startActivity(intent);
        });

        txtTest3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    TestLedMainActivity.class);
            startActivity(intent);
        });

    }
}