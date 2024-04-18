package com.example.una_test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_main_page);

        View isLockOrNot = findViewById(R.id.view_lock_bg);
        Group groupLock = findViewById(R.id.group_locked);
        Group groupUnlock = findViewById(R.id.group_unlock);

        View modeSelectBtn = findViewById(R.id.view_mode_select_bg);
        Group groupModeSelectBtn = findViewById(R.id.group_mode);
        Space mode1 = findViewById(R.id.space_mode_1);
        Space mode2 = findViewById(R.id.space_mode_2);
        Space mode3 = findViewById(R.id.space_mode_3);
        View modeSpace = findViewById(R.id.view_mode_select_up_bg);

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
            modeSelect(mMode);
            groupModeSelectBtn.setVisibility(View.VISIBLE);
        });

        mode1.setOnClickListener(v -> {
            mMode = 1;
            modeSelect(mMode);
            groupModeSelectBtn.setVisibility(View.INVISIBLE);
        });

        mode2.setOnClickListener(v -> {
            mMode = 2;
            modeSelect(mMode);
            groupModeSelectBtn.setVisibility(View.INVISIBLE);
        });

        mode3.setOnClickListener(v -> {
            mMode = 3;
            modeSelect(mMode);
            groupModeSelectBtn.setVisibility(View.INVISIBLE);
        });

        modeSpace.setOnClickListener(v -> groupModeSelectBtn.setVisibility(View.INVISIBLE));

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mNameData = Objects.requireNonNull(result.getData())
                                .getIntExtra("name", 0);
                        txtChargerName.setText(String
                                .format(getString(R.string.txt_name), mNameData + 1));
                    }
                });

        imgBarItemEV.setOnClickListener(v -> {
            Intent intent = new Intent(ChargingMainActivity.this
                    , ChargingDeviceManagementActivity.class);
            resultLauncher.launch(intent);
        });

    }

    private void modeSelect(int mode) {
        ImageView imgMode1 = findViewById(R.id.img_mode_1);
        TextView txtMode1 = findViewById(R.id.txt_mode_1);
        Group groupMode1 = findViewById(R.id.group_mode_realtime);
        ImageView imgMode2 = findViewById(R.id.img_mode_2);
        TextView txtMode2 = findViewById(R.id.txt_mode_2);
        Group groupMode2 = findViewById(R.id.group_mode_quantitative);
        ImageView imgMode3 = findViewById(R.id.img_mode_3);
        TextView txtMode3 = findViewById(R.id.txt_mode_3);
        Group groupMode3 = findViewById(R.id.group_mode_fixed_time);

        switch (mode) {
            case 1:
                imgMode1.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                imgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                imgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                txtMode1.setTextColor(colorBlue);
                txtMode2.setTextColor(Color.WHITE);
                txtMode3.setTextColor(Color.WHITE);
                groupMode1.setVisibility(View.VISIBLE);
                groupMode2.setVisibility(View.INVISIBLE);
                groupMode3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                imgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                imgMode2.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                imgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                txtMode1.setTextColor(Color.WHITE);
                txtMode2.setTextColor(colorBlue);
                txtMode3.setTextColor(Color.WHITE);
                groupMode1.setVisibility(View.INVISIBLE);
                groupMode2.setVisibility(View.VISIBLE);
                groupMode3.setVisibility(View.INVISIBLE);
                break;
            default:
                imgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                imgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                imgMode3.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                txtMode1.setTextColor(Color.WHITE);
                txtMode2.setTextColor(Color.WHITE);
                txtMode3.setTextColor(colorBlue);
                groupMode1.setVisibility(View.INVISIBLE);
                groupMode2.setVisibility(View.INVISIBLE);
                groupMode3.setVisibility(View.VISIBLE);
        }
    }
}
