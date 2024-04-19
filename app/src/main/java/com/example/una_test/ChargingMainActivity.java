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
    private ImageView imgMode1, imgMode2, imgMode3;
    private TextView txtMode1, txtMode2, txtMode3;
    private Group groupMode1, groupMode2, groupMode3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_main_page);

        View isLockOrNot = findViewById(R.id.view_lock_bg);
        Group groupLock = findViewById(R.id.group_locked);
        Group groupUnlock = findViewById(R.id.group_unlock);

        View modeSelectBtn = findViewById(R.id.view_mode_select_bg);

        Dialog mDialogMode = new Dialog(ChargingMainActivity.this, R.style.dialogMode);
        View mViewDialog = View.inflate(ChargingMainActivity.this,
                R.layout.test_2_mode_select_dialog, null);
        mDialogMode.setContentView(mViewDialog);

        Space mode1 = mViewDialog.findViewById(R.id.space_mode_1);
        Space mode2 = mViewDialog.findViewById(R.id.space_mode_2);
        Space mode3 = mViewDialog.findViewById(R.id.space_mode_3);
        imgMode1 = mViewDialog.findViewById(R.id.img_mode_1);
        imgMode2 = mViewDialog.findViewById(R.id.img_mode_2);
        imgMode3 = mViewDialog.findViewById(R.id.img_mode_3);
        txtMode1 = mViewDialog.findViewById(R.id.txt_mode_1);
        txtMode2 = mViewDialog.findViewById(R.id.txt_mode_2);
        txtMode3 = mViewDialog.findViewById(R.id.txt_mode_3);
        groupMode1 = findViewById(R.id.group_mode_realtime);
        groupMode2 = findViewById(R.id.group_mode_quantitative);
        groupMode3 = findViewById(R.id.group_mode_fixed_time);

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
            mDialogMode.show();
            Objects.requireNonNull(mDialogMode.getWindow()).setGravity(Gravity.BOTTOM);
            float height = 235 * ((float) ChargingMainActivity.this.getResources()
                    .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            mDialogMode.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) height);
        });

        mode1.setOnClickListener(v -> {
            mMode = 1;
            modeSelect(mMode);
            mDialogMode.dismiss();
        });

        mode2.setOnClickListener(v -> {
            mMode = 2;
            modeSelect(mMode);
            mDialogMode.dismiss();
        });

        mode3.setOnClickListener(v -> {
            mMode = 3;
            modeSelect(mMode);
            mDialogMode.dismiss();
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

    private void modeSelect(int mode) {
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
