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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_try_catch);
        findViewById(R.id.btn_input).setOnClickListener(mInputExceptionListener);
    }

    private final View.OnClickListener mInputExceptionListener = v -> {
        EditText editText = findViewById(R.id.edit_text);
        TextView txtAns = findViewById(R.id.txt_ans);
        String TAG = "TRY_CATCH";
        try {
            Log.d(TAG, "try start");
            if (editText.getText().toString().isEmpty()) {
                Log.d(TAG, "throw NullPointerException");
                throw new NullPointerException();
            }
            Log.d(TAG, "no null pointer exception");
            int inputText = Integer.parseInt(editText.getText().toString());
            Log.d(TAG, "no number format exception");
            txtAns.setText(TestTryCatchActivity.this.getString(R.string.label_input_sign, inputText));
        } catch (NullPointerException e) {
            txtAns.setText(R.string.label_no_input);
            Log.d(TAG, "NullPointerException");
        } catch (NumberFormatException e) {
            txtAns.setText(R.string.label_input_format_error);
            Log.d(TAG, "NumberFormatException");
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
