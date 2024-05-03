package com.example.una_test;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestLedMainActivity extends AppCompatActivity {

    private static int mBattleBright = 30;
    String TAG = "TestLed";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_led_main);

        ConstraintLayout clBattleMode = findViewById(R.id.cl_battle_mode);
        ConstraintLayout clCustomization = findViewById(R.id.cl_customization);
        ConstraintLayout clBattleModeItem = findViewById(R.id.cl_battle_mode_item);
        ConstraintLayout clRecalibrateBtn = findViewById(R.id.cl_recalibrate_btn);
        ExpandableListView exBattleModeView = findViewById(R.id.ex_battle_mode_list_view);
        ExpandableListView exCustomizationItem = findViewById(R.id.ex_customization_list_view);
        SeekBar seekBarBrightnessBattle = findViewById(R.id.seek_bar_brightness);
        TextView txtCancel = findViewById(R.id.txt_cancel);
        TextView txtSetting = findViewById(R.id.txt_use_setting);
        ImageView imgHint = findViewById(R.id.img_hint);

        final List<ExpandableAdapter.GroupItem> battleGroup = Arrays.asList(
                new ExpandableAdapter.GroupItem("Accelerating", R.drawable.aecelerating,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.SCAN, LedColor.GREEN, 10))),
                new ExpandableAdapter.GroupItem("Constant Speed", R.drawable.constant,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.GSENSOR, LedColor.RED, 20))),
                new ExpandableAdapter.GroupItem("Decelerating", R.drawable.decelerating,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.SCAN, LedColor.GREEN, 30))));

        final List<ExpandableAdapter.GroupItem> customGroup = Arrays.asList(
                new ExpandableAdapter.GroupItem("Accelerating", R.drawable.aecelerating,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.CLOSE, LedColor.NULL, 10, 10,
                                mLedListener, mColorListener, mBrightListener, mSpeedListener))),
                new ExpandableAdapter.GroupItem("Constant Speed", R.drawable.constant,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.CLOSE, LedColor.NULL, 20, 20,
                                mLedListener, mColorListener, mBrightListener, mSpeedListener))),
                new ExpandableAdapter.GroupItem("Decelerating", R.drawable.decelerating,
                        Collections.singletonList(new ExpandableAdapter.ChildItem(
                                LEDMode.CLOSE, LedColor.NULL, 30, 30,
                                mLedListener, mColorListener, mBrightListener, mSpeedListener))));

        ExpandableAdapter battleModeAdapter = new ExpandableAdapter(this, battleGroup);
        ExpandableAdapter customizationAdapter = new ExpandableAdapter(this, customGroup);
        exBattleModeView.setAdapter(battleModeAdapter);
        exCustomizationItem.setAdapter(customizationAdapter);

        for (int i = 0; i < battleGroup.size(); i++) {
            exBattleModeView.expandGroup(i);
            exCustomizationItem.expandGroup(i);
        }

        clBattleMode.setOnClickListener(v -> {
            clBattleModeItem.setVisibility(View.VISIBLE);
            exCustomizationItem.setVisibility(View.INVISIBLE);
            clBattleMode.getBackground().setTint(
                    getResources().getColor(R.color.black2, getTheme()));
            clCustomization.getBackground().setTint(
                    getResources().getColor(R.color.transparent, getTheme()));
        });

        clCustomization.setOnClickListener(v -> {
            clBattleModeItem.setVisibility(View.INVISIBLE);
            exCustomizationItem.setVisibility(View.VISIBLE);
            clBattleMode.getBackground().setTint(
                    getResources().getColor(R.color.transparent, getTheme()));
            clCustomization.getBackground().setTint(
                    getResources().getColor(R.color.black2, getTheme()));
        });

        seekBarBrightnessBattle.setProgress(mBattleBright);
        seekBarBrightnessBattle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBattleBright = seekBar.getProgress();
                Log.e(TAG, "battle bright " + mBattleBright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        txtCancel.setOnClickListener(v -> finish());
        txtSetting.setOnClickListener(v -> finish());
        clRecalibrateBtn.setOnClickListener(v -> Toast.makeText(TestLedMainActivity.this,
                "Recalibrate Button", Toast.LENGTH_SHORT).show());
        imgHint.setOnClickListener(v -> Toast.makeText(TestLedMainActivity.this,
                "Hint Button", Toast.LENGTH_SHORT).show());
    }

    private final ExpandableAdapter.ChildItem.LedModeListener mLedListener = (groupMode, mode) ->
            Log.e(TAG, groupMode + " LED listener " + getResources().getString(mode.ledName()));

    private final ExpandableAdapter.ChildItem.ColorListener mColorListener = (groupMode, color) ->
            Log.e(TAG, groupMode + " color listener " + getResources().getString(color.ledColor()));

    private final ExpandableAdapter.ChildItem.BrightListener mBrightListener = (groupMode, progress) ->
            Log.e(TAG, groupMode + " bright listener " + progress);

    private final ExpandableAdapter.ChildItem.SpeedListener mSpeedListener = (groupMode, progress) ->
            Log.e(TAG, groupMode + " speed listener " + progress);

    private enum LEDMode {
        CLOSE, SCAN, GSENSOR;

        private int ledName() {
            switch (this) {
                case SCAN:
                    return R.string.label_scan_mode;
                case GSENSOR:
                    return R.string.label_gsensor_settings;
                default:
                    return R.string.label_select;
            }
        }
    }

    private enum LedColor {
        RED, GREEN, BLUE, NULL;

        private int ledColor() {
            switch (this) {
                case RED:
                    return R.color.red;
                case BLUE:
                    return R.color.blue1;
                case GREEN:
                    return R.color.green;
                default:
                    return -1;
            }
        }
    }

    private static class ExpandableAdapter extends BaseExpandableListAdapter {
        private final List<GroupItem> mGroup;
        private final Context mContext;
        private final LayoutInflater mLayoutInflater;
        private ImageView mImgIndicator;

        private static class GroupItem {
            private final String mMode;
            private final int mImg;
            private final List<ChildItem> mChild;

            GroupItem(String mode, int img, List<ChildItem> child) {
                this.mMode = mode;
                this.mImg = img;
                this.mChild = child;
            }
        }

        private static class ChildItem {
            private interface LedModeListener {
                void listener(String groupMode, LEDMode mode);
            }

            private interface ColorListener {
                void listener(String groupMode, LedColor color);
            }

            private interface BrightListener {
                void listener(String groupMode, int progress);
            }

            private interface SpeedListener {
                void listener(String groupMode, int progress);
            }

            private LEDMode mLedMode;
            private LedColor mColor;
            private int mBrightness, mSpeed;
            private final LedModeListener mLedListener;
            private final ColorListener mColorListener;
            private final BrightListener mBrightListener;
            private final SpeedListener mSpeedListener;

            private ChildItem(LEDMode ledMode, LedColor color, int speed) {
                this.mLedMode = ledMode;
                this.mColor = color;
                this.mBrightness = -1;
                this.mSpeed = speed;
                this.mLedListener = null;
                this.mColorListener = null;
                this.mBrightListener = null;
                this.mSpeedListener = null;
            }

            private ChildItem(LEDMode ledMode, LedColor color, int brightness, int speed,
                              LedModeListener ledListener, ColorListener colorListener,
                              BrightListener brightListener, SpeedListener speedListener) {
                this.mLedMode = ledMode;
                this.mLedListener = ledListener;
                this.mColor = color;
                this.mColorListener = colorListener;
                this.mBrightness = brightness;
                this.mBrightListener = brightListener;
                this.mSpeed = speed;
                this.mSpeedListener = speedListener;
            }
        }

        ExpandableAdapter(Context context, List<GroupItem> group) {
            this.mGroup = group;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mContext = context;
        }

        @Override
        public int getGroupCount() {
            return mGroup.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroup.get(groupPosition).mChild.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroup.get(groupPosition);
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return mGroup.get(groupPosition).mChild.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private static class GroupViewHolder {
            TextView txtTitle;
            ImageView imgTitle;

            GroupViewHolder(View view) {
                txtTitle = view.findViewById(R.id.txt_title);
                imgTitle = view.findViewById(R.id.img_title);
            }
        }

        private static class ChildViewHolder {
            ConstraintLayout clBrightnessItem, clLedCustom, clColorCustom, clLedItem,
                    clColorItem, clSpeedItem;
            TextView txtLed, txtColor;
            SeekBar seekBarSpeed, seekBarBrightness;
            ImageView imgColor;

            ChildViewHolder(View view) {
                clLedItem = view.findViewById(R.id.cl_led_item);
                clLedCustom = view.findViewById(R.id.cl_customization_led);
                txtLed = view.findViewById(R.id.txt_led);
                clColorItem = view.findViewById(R.id.cl_color_item);
                clColorCustom = view.findViewById(R.id.cl_customization_color);
                imgColor = view.findViewById(R.id.img_color);
                txtColor = view.findViewById(R.id.txt_color);
                clBrightnessItem = view.findViewById(R.id.cl_brightness_item);
                seekBarBrightness = view.findViewById(R.id.seek_bar_brightness);
                clSpeedItem = view.findViewById(R.id.cl_speed_item);
                seekBarSpeed = view.findViewById(R.id.seek_bar_speed);
            }
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.expandable_list_view_item,
                        parent, false);
                groupViewHolder = new GroupViewHolder(convertView);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            GroupItem item = mGroup.get(groupPosition);
            groupViewHolder.txtTitle.setText(item.mMode);
            groupViewHolder.imgTitle.setImageResource(item.mImg);
            mImgIndicator = convertView.findViewById(R.id.img_indicator);
            mImgIndicator.setSelected(false);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.expandable_list_view_childitem,
                        parent, false);
                childViewHolder = new ChildViewHolder(convertView);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            mImgIndicator.setSelected(true);

            String groupMode = mGroup.get(groupPosition).mMode;
            ChildItem item = mGroup.get(groupPosition).mChild.get(childPosition);

            childViewHolder.seekBarSpeed.setProgress(item.mSpeed);
            childViewHolder.txtLed.setText(item.mLedMode.ledName());

            if (item.mBrightListener != null) {
                childViewHolder.seekBarBrightness.setProgress(item.mBrightness);
                childViewHolder.seekBarBrightness.setOnSeekBarChangeListener(new SeekBar
                        .OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        item.mBrightness = childViewHolder.seekBarBrightness.getProgress();
                        item.mBrightListener.listener(groupMode, item.mBrightness);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            } else {
                childViewHolder.clBrightnessItem.setVisibility(View.GONE);
                childViewHolder.seekBarBrightness.setOnSeekBarChangeListener(null);
            }

            if (item.mSpeedListener != null) {
                childViewHolder.seekBarSpeed.setOnSeekBarChangeListener(new SeekBar
                        .OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        item.mSpeed = childViewHolder.seekBarSpeed.getProgress();
                        item.mSpeedListener.listener(groupMode, item.mSpeed);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            } else {
                childViewHolder.seekBarSpeed.setOnSeekBarChangeListener(null);
                childViewHolder.clSpeedItem.setEnabled(false);
                childViewHolder.seekBarSpeed.setEnabled(false);
            }

            if (item.mLedListener != null) {
                childViewHolder.clLedCustom.setOnClickListener(v -> {
                    if (item.mLedMode == LEDMode.SCAN) {
                        item.mLedMode = LEDMode.GSENSOR;
                    } else {
                        item.mLedMode = LEDMode.SCAN;
                    }
                    notifyDataSetChanged();
                    item.mLedListener.listener(groupMode, item.mLedMode);
                });
            } else {
                childViewHolder.clLedCustom.setOnClickListener(null);
                childViewHolder.clLedItem.setEnabled(false);
                childViewHolder.clLedCustom.setEnabled(false);
            }

            if (item.mColorListener != null) {
                boolean isEmpty = (item.mColor == LedColor.NULL);
                childViewHolder.imgColor.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                childViewHolder.txtColor.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
                childViewHolder.imgColor.setImageTintList(isEmpty ? null : ColorStateList
                        .valueOf(mContext.getResources().getColor(item.mColor.ledColor(),
                                mContext.getTheme())));

                childViewHolder.clColorCustom.setOnClickListener(v -> {
                    if (item.mColor == LedColor.GREEN) {
                        item.mColor = LedColor.BLUE;
                    } else if (item.mColor == LedColor.RED) {
                        item.mColor = LedColor.GREEN;
                    } else {
                        item.mColor = LedColor.RED;
                    }
                    notifyDataSetChanged();
                    item.mColorListener.listener(groupMode, item.mColor);
                });
            } else {
                childViewHolder.clColorCustom.setOnClickListener(null);
                childViewHolder.txtColor.setVisibility(View.INVISIBLE);
                childViewHolder.clColorItem.setEnabled(false);
                childViewHolder.clColorCustom.setEnabled(false);
                childViewHolder.imgColor.setImageTintList(ColorStateList.valueOf(mContext
                        .getResources().getColor(item.mColor.ledColor(), mContext.getTheme())));
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}