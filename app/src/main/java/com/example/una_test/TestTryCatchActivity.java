package com.example.una_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestTryCatchActivity extends AppCompatActivity {
    private boolean isColorBlue = true;
    String TAG = "TRY_CATCH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_try_catch);
        findViewById(R.id.btn_input).setOnClickListener(mInputExceptionListener);

        EditText editNum1 = findViewById(R.id.edit_num1);
        EditText editNum2 = findViewById(R.id.edit_num2);
        TextView txtAns2 = findViewById(R.id.txt_ans2);
        TextView txtHint = findViewById(R.id.txt_hint);
        txtAns2.setText(getString(R.string.label_equal_sign, 0.0));

        findViewById(R.id.btn_compute).setOnClickListener(v -> {
            try {
                float ans = compute(editNum1, editNum2);
                txtAns2.setText(getString(R.string.label_equal_sign, ans));
                txtHint.setText("");
            } catch (NumberFormatException | ArithmeticException e) {
                Log.d(TAG, e.toString());
                txtHint.setText(e.getMessage());
            }
        });
    }

    private float compute(EditText inputNum1, EditText inputNum2) {
        int num1 = checkNum(inputNum1);
        int num2 = checkNum(inputNum2);
        if (num2 == 0) {
            throw new ArithmeticException("Arithmetic error");
        }
        Log.d(TAG, "compute end");
        return (float) num1 / num2;
    }

    private int checkNum(EditText text) {
        try {
            return Integer.parseInt(text.getText().toString());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NumberFormat error");
        }
    }

    private final View.OnClickListener mInputExceptionListener = v -> {
        EditText editText = findViewById(R.id.edit_text);
        TextView txtAns = findViewById(R.id.txt_ans);
        try {
            Log.d(TAG, "try start");
            if (editText.getText().toString().isEmpty()) {
                Log.d(TAG, "throw NullPointerException");
                throw new NullPointerException();
            }
            Log.d(TAG, "no null pointer exception");
            int inputText = Integer.parseInt(editText.getText().toString());
            Log.d(TAG, "no number format exception");
            txtAns.setText(getString(R.string.label_input_sign, inputText));
        } catch (NullPointerException e) {
            txtAns.setText(R.string.label_no_input);
            Log.d(TAG, e.toString());
        } catch (NumberFormatException e) {
            txtAns.setText(R.string.label_input_format_error);
            Log.d(TAG, e.toString());
        } finally {
            if (isColorBlue) {
                txtAns.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                isColorBlue = false;
            } else {
                txtAns.setBackgroundColor(getResources().getColor(R.color.blue1, getTheme()));
                isColorBlue = true;
            }
            Log.d(TAG, "finally block background color switch");
        }
        Log.d(TAG, "try catch finally block end");
        txtAns.setText(String.format("%s, %s", txtAns.getText(),
                getString(R.string.label_try_catch_end)));
    };
}
