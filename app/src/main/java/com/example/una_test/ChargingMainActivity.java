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
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.una_test.databinding.ActivityChargingMainPageBinding;
import com.example.una_test.databinding.Test2ModeSelectDialogBinding;

import java.util.Objects;

public class ChargingMainActivity extends AppCompatActivity {
    private boolean mIsLock = true;
    private final int colorBlue = Color.parseColor("#0094D6");
    private int mMode = 1;
    private int mNameData = 1;
    private ActivityChargingMainPageBinding binding;
    private Test2ModeSelectDialogBinding selectBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charging_main_page);

        ImageView imgBarItemEV = findViewById(R.id.img_bar_item_ev);

        TextView txtChargerName = findViewById(R.id.txt_charger_name);
        txtChargerName.setText(String.format(getString(R.string.txt_name), mNameData));

        binding.viewLockBg.setOnClickListener(v -> {
            if (mIsLock) {
                binding.groupLocked.setVisibility(View.INVISIBLE);
                binding.groupUnlock.setVisibility(View.VISIBLE);
                mIsLock = false;
            } else {
                binding.groupLocked.setVisibility(View.VISIBLE);
                binding.groupUnlock.setVisibility(View.INVISIBLE);
                mIsLock = true;
            }
        });

        binding.viewModeSelectBg.setOnClickListener(v -> {
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

    private void showModeDialog() {
        Dialog modeDialog = new Dialog(ChargingMainActivity.this, R.style.dialogMode);
        View viewDialog = View.inflate(ChargingMainActivity.this,
                R.layout.test_2_mode_select_dialog, null);
        selectBinding = Test2ModeSelectDialogBinding.bind(viewDialog);
        modeDialog.setContentView(viewDialog);

        modeDialog.show();
        Objects.requireNonNull(modeDialog.getWindow()).setGravity(Gravity.BOTTOM);
        float height = 235 * ((float) ChargingMainActivity.this.getResources()
                .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        modeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) height);

        selectBinding.spaceMode1.setOnClickListener(v -> {
            mMode = 1;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        selectBinding.spaceMode2.setOnClickListener(v -> {
            mMode = 2;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        selectBinding.spaceMode3.setOnClickListener(v -> {
            mMode = 3;
            modeSelect(mMode);
            modeDialog.dismiss();
        });
    }

    private void modeSelect(int mode) {
        switch (mode) {
            case 1:
                selectBinding.imgMode1.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.txtMode1.setTextColor(colorBlue);
                selectBinding.txtMode2.setTextColor(Color.WHITE);
                selectBinding.txtMode3.setTextColor(Color.WHITE);
                binding.groupModeRealtime.setVisibility(View.VISIBLE);
                binding.groupModeQuantitative.setVisibility(View.INVISIBLE);
                binding.groupModeFixedTime.setVisibility(View.INVISIBLE);
                break;
            case 2:
                selectBinding.imgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode2.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.txtMode1.setTextColor(Color.WHITE);
                selectBinding.txtMode2.setTextColor(colorBlue);
                selectBinding.txtMode3.setTextColor(Color.WHITE);
                binding.groupModeRealtime.setVisibility(View.INVISIBLE);
                binding.groupModeQuantitative.setVisibility(View.VISIBLE);
                binding.groupModeFixedTime.setVisibility(View.INVISIBLE);
                break;
            default:
                selectBinding.imgMode1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                selectBinding.imgMode3.setColorFilter(colorBlue, PorterDuff.Mode.SRC_ATOP);
                selectBinding.txtMode1.setTextColor(Color.WHITE);
                selectBinding.txtMode2.setTextColor(Color.WHITE);
                selectBinding.txtMode3.setTextColor(colorBlue);
                binding.groupModeRealtime.setVisibility(View.INVISIBLE);
                binding.groupModeQuantitative.setVisibility(View.INVISIBLE);
                binding.groupModeFixedTime.setVisibility(View.VISIBLE);
        }
    }
}

