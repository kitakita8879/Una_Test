package com.example.una_test;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditTextActivity extends AppCompatActivity {
    private TextView mTxt1, mTxtBefore, mTxtAfter;
    private EditText mEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        mTxt1 = findViewById(R.id.txt_1);
        mTxtBefore = findViewById(R.id.txt_before);
        mTxtAfter = findViewById(R.id.txt_after);
        mEdittext = findViewById(R.id.edt_1);
        mEdittext.addTextChangedListener(mWatcher);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() == mEdittext && mEdittext.getShowSoftInputOnFocus()) {
                mEdittext.clearFocus();
                InputMethodManager methodManager = (InputMethodManager) getSystemService(Activity
                        .INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(mEdittext.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private final TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mTxtBefore.setText(String.format("before: %s", s));
            mTxtAfter.setText("");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTxt1.setText(String.format("already input: %s /20", s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
            mTxtAfter.setText(String.format("after: %s", s));
        }
    };
}
