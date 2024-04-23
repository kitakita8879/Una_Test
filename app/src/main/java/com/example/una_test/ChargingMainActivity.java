package com.example.una_test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.util.Objects;

public class ChargingMainActivity extends AppCompatActivity {
    private boolean mIsLock = true;
    private final int colorBlue = Color.parseColor("#0094D6");
    private int mMode = 1;
    private int mNameData = 1;
    private ImageView mImgMode1, mImgMode2, mImgMode3;
    private TextView mTxtMode1, mTxtMode2, mTxtMode3;
    private Group mGroupMode1, mGroupMode2, mGroupMode3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_main_page);

        View isLockOrNot = findViewById(R.id.view_lock_bg);
        Group groupLock = findViewById(R.id.group_locked);
        Group groupUnlock = findViewById(R.id.group_unlock);

        View modeSelectBtn = findViewById(R.id.view_mode_select_bg);
        mGroupMode1 = findViewById(R.id.group_mode_realtime);
        mGroupMode2 = findViewById(R.id.group_mode_quantitative);
        mGroupMode3 = findViewById(R.id.group_mode_fixed_time);

        ImageView imgBarItemEV = findViewById(R.id.img_bar_item_ev);

        TextView txtChargerName = findViewById(R.id.txt_charger_name);
        txtChargerName.setText(String.format(getString(R.string.txt_name), mNameData));

        isLockOrNot.setOnClickListener(v -> {
            if (mIsLock) {
                groupLock.setVisibility(View.INVISIBLE);
                groupUnlock.setVisibility(View.VISIBLE);
                mIsLock = false;
            } else {
                groupLock.setVisibility(View.VISIBLE);
                groupUnlock.setVisibility(View.INVISIBLE);
                mIsLock = true;
            }
        });

        modeSelectBtn.setOnClickListener(v -> {
            showModeDialog();
            modeSelect(mMode);
        });

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mNameData = Objects.requireNonNull(result.getData())
                                .getIntExtra("name", 1);
                        txtChargerName.setText(String
                                .format(getString(R.string.txt_name), mNameData));
                    }
                });

        imgBarItemEV.setOnClickListener(v -> {
            Intent intent = new Intent(ChargingMainActivity.this
                    , ChargingDeviceManagementActivity.class);
            resultLauncher.launch(intent);
        });
    }

    private void showModeDialog(){
        Dialog modeDialog = new Dialog(ChargingMainActivity.this, R.style.dialogMode);
        View viewDialog = View.inflate(ChargingMainActivity.this,
                R.layout.test_2_mode_select_dialog, null);
        modeDialog.setContentView(viewDialog);
        Space mode1 = viewDialog.findViewById(R.id.space_mode_1);
        Space mode2 = viewDialog.findViewById(R.id.space_mode_2);
        Space mode3 = viewDialog.findViewById(R.id.space_mode_3);
        mImgMode1 = viewDialog.findViewById(R.id.img_mode_1);
        mImgMode2 = viewDialog.findViewById(R.id.img_mode_2);
        mImgMode3 = viewDialog.findViewById(R.id.img_mode_3);
        mTxtMode1 = viewDialog.findViewById(R.id.txt_mode_1);
        mTxtMode2 = viewDialog.findViewById(R.id.txt_mode_2);
        mTxtMode3 = viewDialog.findViewById(R.id.txt_mode_3);

        modeDialog.show();
        Objects.requireNonNull(modeDialog.getWindow()).setGravity(Gravity.BOTTOM);
        float height = 235 * ((float) ChargingMainActivity.this.getResources()
                .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        modeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) height);

        mode1.setOnClickListener(v -> {
            mMode = 1;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        mode2.setOnClickListener(v -> {
            mMode = 2;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        mode3.setOnClickListener(v -> {
            mMode = 3;
            modeSelect(mMode);
            modeDialog.dismiss();
        });
    }

    private void modeSelect(int mode) {
        switch (mode) {
            case 1:
                mImgMode1.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                mImgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mImgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mTxtMode1.setTextColor(colorBlue);
                mTxtMode2.setTextColor(Color.WHITE);
                mTxtMode3.setTextColor(Color.WHITE);
                mGroupMode1.setVisibility(View.VISIBLE);
                mGroupMode2.setVisibility(View.INVISIBLE);
                mGroupMode3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mImgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mImgMode2.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                mImgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mTxtMode1.setTextColor(Color.WHITE);
                mTxtMode2.setTextColor(colorBlue);
                mTxtMode3.setTextColor(Color.WHITE);
                mGroupMode1.setVisibility(View.INVISIBLE);
                mGroupMode2.setVisibility(View.VISIBLE);
                mGroupMode3.setVisibility(View.INVISIBLE);
                break;
            default:
                mImgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mImgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mImgMode3.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                mTxtMode1.setTextColor(Color.WHITE);
                mTxtMode2.setTextColor(Color.WHITE);
                mTxtMode3.setTextColor(colorBlue);
                mGroupMode1.setVisibility(View.INVISIBLE);
                mGroupMode2.setVisibility(View.INVISIBLE);
                mGroupMode3.setVisibility(View.VISIBLE);
        }
    }
}

