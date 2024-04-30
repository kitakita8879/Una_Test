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
import androidx.databinding.DataBindingUtil;

import com.example.una_test.databinding.ActivityChargingMainPageBinding;
import com.example.una_test.databinding.Test2ModeSelectDialogBinding;

import java.util.Objects;

public class ChargingMainActivity extends AppCompatActivity {
    private ModeType mMode = ModeType.REAL_TIME;
    private int mNameData = 1;
    private ActivityChargingMainPageBinding mBinding;
    private Test2ModeSelectDialogBinding mSelectBinding;
    private Dialog mModeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_charging_main_page);

        mBinding.txtChargerName.setText(getString(R.string.label_my_charger, mNameData));
        mBinding.viewLockBg.setOnClickListener(v -> mBinding.setUnlock(!mBinding.getUnlock()));
        mBinding.setMode(mMode);
        mBinding.viewModeSelectBg.setOnClickListener(v -> {
            showModeDialog();
            mSelectBinding.setMode(mMode);
        });

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mNameData = Objects.requireNonNull(result.getData())
                                .getIntExtra("name", 1);
                        mBinding.txtChargerName.setText(getString(R.string.label_my_charger, mNameData));
                    }
                });

        mBinding.imgBarItemEv.setOnClickListener(v -> {
            Intent intent = new Intent(ChargingMainActivity.this
                    , ChargingDeviceManagementActivity.class);
            resultLauncher.launch(intent);
        });
    }

    private void showModeDialog() {
        mModeDialog = new Dialog(this, R.style.dialogMode);
        View viewDialog = View.inflate(this, R.layout.test_2_mode_select_dialog, null);
        mSelectBinding = Test2ModeSelectDialogBinding.bind(viewDialog);
        mModeDialog.setContentView(viewDialog);

        mModeDialog.show();
        Objects.requireNonNull(mModeDialog.getWindow()).setGravity(Gravity.BOTTOM);
        float height = 235 * ((float) ChargingMainActivity.this.getResources()
                .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        mModeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) height);
        mSelectBinding.setActivity(this);
    }

    public void modeSelect(ModeType mode) {
        if (mMode == mode) {
            mModeDialog.dismiss();
            return;
        }
        mMode = mode;
        mBinding.setMode(mMode);
        mModeDialog.dismiss();
    }

    public enum ModeType {
        REAL_TIME, QUANTITATIVE, FIXED_TIME
    }
}
