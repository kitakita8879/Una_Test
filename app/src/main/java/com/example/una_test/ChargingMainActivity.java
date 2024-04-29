package com.example.una_test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.una_test.databinding.ActivityChargingMainPageBinding;
import com.example.una_test.databinding.Test2ModeSelectDialogBinding;

import java.util.Objects;

public class ChargingMainActivity extends AppCompatActivity {
    private int mMode = 1;
    private int mNameData = 1;
    private ActivityChargingMainPageBinding mBinding;
    private Test2ModeSelectDialogBinding mSelectBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_charging_main_page);

        mBinding.txtChargerName.setText(String.format(getString(R.string.label_my_charger), mNameData));
        mBinding.viewLockBg.setOnClickListener(v -> mBinding.setUnlock(!mBinding.getUnlock()));
        mBinding.viewModeSelectBg.setOnClickListener(v -> {
            showModeDialog();
            modeSelect(mMode);
        });

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mNameData = Objects.requireNonNull(result.getData())
                                .getIntExtra("name", 1);
                        mBinding.txtChargerName.setText(String
                                .format(getString(R.string.label_my_charger), mNameData));
                    }
                });

        mBinding.imgBarItemEv.setOnClickListener(v -> {
            Intent intent = new Intent(ChargingMainActivity.this
                    , ChargingDeviceManagementActivity.class);
            resultLauncher.launch(intent);
        });
    }

    private void showModeDialog() {
        Dialog modeDialog = new Dialog(this, R.style.dialogMode);
        View viewDialog = View.inflate(this, R.layout.test_2_mode_select_dialog, null);
        mSelectBinding = Test2ModeSelectDialogBinding.bind(viewDialog);
        modeDialog.setContentView(viewDialog);

        modeDialog.show();
        Objects.requireNonNull(modeDialog.getWindow()).setGravity(Gravity.BOTTOM);
        float height = 235 * ((float) ChargingMainActivity.this.getResources()
                .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        modeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) height);

        mSelectBinding.viewMode1.setOnClickListener(v -> {
            mMode = 1;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        mSelectBinding.viewMode2.setOnClickListener(v -> {
            mMode = 2;
            modeSelect(mMode);
            modeDialog.dismiss();
        });

        mSelectBinding.viewMode3.setOnClickListener(v -> {
            mMode = 3;
            modeSelect(mMode);
            modeDialog.dismiss();
        });
    }

    private void modeSelect(int mode) {
        switch (mode) {
            case 1:
                mSelectBinding.txtMode1.setSelected(true);
                mSelectBinding.imgMode1.setSelected(true);
                mBinding.imgMode.setImageDrawable(ContextCompat
                        .getDrawable(this, R.drawable.ic_realtime_charge));
                mBinding.txtMode.setText(R.string.label_real_time_charging_upper);
                break;
            case 2:
                mSelectBinding.txtMode2.setSelected(true);
                mSelectBinding.imgMode2.setSelected(true);
                mBinding.imgMode.setImageDrawable(ContextCompat
                        .getDrawable(this, R.drawable.ic_quantitative_charge));
                mBinding.txtMode.setText(R.string.label_quantitative_charging_upper);
                break;
            default:
                mSelectBinding.txtMode3.setSelected(true);
                mSelectBinding.imgMode3.setSelected(true);
                mBinding.imgMode.setImageDrawable(ContextCompat
                        .getDrawable(this, R.drawable.ic_fixedtime_charge));
                mBinding.txtMode.setText(R.string.label_fixed_time_charging_upper);
        }
    }
}

