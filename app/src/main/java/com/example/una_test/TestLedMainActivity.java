package com.example.una_test;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
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
    private final List<GroupItem> group = Arrays.asList(
            new GroupItem("Accelerating", R.drawable.aecelerating),
            new GroupItem("Constant Speed", R.drawable.constant),
            new GroupItem("Decelerating", R.drawable.decelerating));
    private final List<List<ChildItem>> battleModeChild = Arrays.asList(
            Collections.singletonList(new ChildItem(R.string.label_scan_mode, R.color.green,
                    30, 10)),
            Collections.singletonList(new ChildItem(R.string.label_gsensor_settings, R.color.red,
                    30, 20)),
            Collections.singletonList(new ChildItem(R.string.label_scan_mode, R.color.green,
                    30, 30)));
    private final List<List<ChildItem>> customizationChild = Arrays.asList(
            Collections.singletonList(new ChildItem(R.string.label_select, R.string.label_select,
                    10, 10)),
            Collections.singletonList(new ChildItem(R.string.label_select, R.string.label_select,
                    20, 20)),
            Collections.singletonList(new ChildItem(R.string.label_select, R.string.label_select,
                    30, 30)));

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

        ExpandableAdapter battleModeAdapter = new ExpandableAdapter(this, group,
                battleModeChild, false);
        ExpandableAdapter customizationAdapter = new ExpandableAdapter(this, group,
                customizationChild, true);
        exBattleModeView.setAdapter(battleModeAdapter);
        exCustomizationItem.setAdapter(customizationAdapter);

        for (int i = 0; i < group.size(); i++) {
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

        seekBarBrightnessBattle.setProgress(battleModeChild.get(0).get(0).mBrightness);
        seekBarBrightnessBattle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                for (int i = 0; i < battleModeChild.size(); i++) {
                    battleModeChild.get(i).get(0).mBrightness = seekBar.getProgress();
                }
            }
        });

        txtCancel.setOnClickListener(v -> finish());
        txtSetting.setOnClickListener(v -> finish());
        clRecalibrateBtn.setOnClickListener(v -> Toast.makeText(TestLedMainActivity.this,
                "Recalibrate Button", Toast.LENGTH_SHORT).show());
        imgHint.setOnClickListener(v -> Toast.makeText(TestLedMainActivity.this,
                "Hint Button", Toast.LENGTH_SHORT).show());
    }

    private static class GroupItem {
        private final String mode;
        private final int img;

        GroupItem(String mode, int img) {
            this.mode = mode;
            this.img = img;
        }
    }

    private static class ChildItem {
        private int mLedMode, mBrightness, mSpeed, mColor;

        ChildItem(int ledMode, int color, int brightness, int speed) {
            this.mLedMode = ledMode;
            this.mBrightness = brightness;
            this.mColor = color;
            this.mSpeed = speed;
        }
    }

    private class ExpandableAdapter extends BaseExpandableListAdapter {
        List<GroupItem> group;
        List<List<ChildItem>> childItem;
        LayoutInflater layoutInflater;
        ImageView imgIndicator;
        boolean isCustomization;

        ExpandableAdapter(Context context, List<GroupItem> group,
                          List<List<ChildItem>> childItem, boolean isCustomization) {
            this.group = group;
            this.childItem = childItem;
            this.layoutInflater = LayoutInflater.from(context);
            this.isCustomization = isCustomization;
        }

        @Override
        public int getGroupCount() {
            return group.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childItem.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childItem.get(groupPosition).get(childPosition);
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

        class GroupViewHolder {
            TextView txtTitle;
            ImageView imgTitle;

            GroupViewHolder(View view) {
                txtTitle = view.findViewById(R.id.txt_title);
                imgTitle = view.findViewById(R.id.img_title);
            }
        }

        class ChildViewHolder {
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
                convertView = layoutInflater.inflate(R.layout.expandable_list_view_item,
                        parent, false);
                groupViewHolder = new GroupViewHolder(convertView);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            groupViewHolder.txtTitle.setText(group.get(groupPosition).mode);
            groupViewHolder.imgTitle.setImageResource(group.get(groupPosition).img);
            imgIndicator = convertView.findViewById(R.id.img_indicator);
            imgIndicator.setSelected(false);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.expandable_list_view_childitem,
                        parent, false);
                childViewHolder = new ChildViewHolder(convertView);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            imgIndicator.setSelected(true);

            childViewHolder.seekBarSpeed.setProgress(childItem.get(groupPosition)
                    .get(childPosition).mSpeed);
            childViewHolder.txtLed.setText(childItem.get(groupPosition).get(childPosition).mLedMode);

            if (isCustomization) {
                childViewHolder.seekBarBrightness.setProgress(childItem
                        .get(groupPosition).get(childPosition).mBrightness);
                if (childItem.get(groupPosition).get(childPosition).mColor == R.string.label_select) {
                    childViewHolder.imgColor.setVisibility(View.INVISIBLE);
                    childViewHolder.txtColor.setVisibility(View.VISIBLE);
                } else {
                    childViewHolder.txtColor.setVisibility(View.INVISIBLE);
                    childViewHolder.imgColor.setVisibility(View.VISIBLE);
                    childViewHolder.imgColor.setImageTintList(ColorStateList.valueOf(getResources()
                            .getColor(childItem.get(groupPosition).get(childPosition).mColor,
                                    getTheme())));
                }
            } else {
                childViewHolder.clBrightnessItem.setVisibility(View.GONE);
                childViewHolder.txtColor.setVisibility(View.INVISIBLE);
                childViewHolder.clSpeedItem.setEnabled(false);
                childViewHolder.seekBarSpeed.setEnabled(false);
                childViewHolder.clLedItem.setEnabled(false);
                childViewHolder.clLedCustom.setEnabled(false);
                childViewHolder.clColorItem.setEnabled(false);
                childViewHolder.clColorCustom.setEnabled(false);
                childViewHolder.imgColor.setImageTintList(ColorStateList.valueOf(getResources()
                        .getColor(childItem.get(groupPosition).get(childPosition).mColor,
                                getTheme())));
            }

            childViewHolder.seekBarSpeed.setOnSeekBarChangeListener(new SeekBar
                    .OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    childItem.get(groupPosition).get(childPosition).mSpeed = childViewHolder
                            .seekBarSpeed.getProgress();
                }
            });
            childViewHolder.seekBarBrightness.setOnSeekBarChangeListener(new SeekBar
                    .OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    childItem.get(groupPosition).get(childPosition).mBrightness = childViewHolder
                            .seekBarBrightness.getProgress();
                }
            });

            childViewHolder.clLedCustom.setOnClickListener(v -> {
                if (childItem.get(groupPosition).get(childPosition).mLedMode == R.string
                        .label_scan_mode) {
                    childItem.get(groupPosition).get(childPosition).mLedMode = R.string
                            .label_gsensor_settings;
                } else {
                    childItem.get(groupPosition).get(childPosition).mLedMode = R.string
                            .label_scan_mode;
                }
                notifyDataSetChanged();
            });

            childViewHolder.clColorCustom.setOnClickListener(v -> {
                childViewHolder.imgColor.setVisibility(View.VISIBLE);
                childViewHolder.txtColor.setVisibility(View.INVISIBLE);
                if (childItem.get(groupPosition).get(childPosition).mColor == R.color.green) {
                    childItem.get(groupPosition).get(childPosition).mColor = R.color.red;
                } else {
                    childItem.get(groupPosition).get(childPosition).mColor = R.color.green;
                }
                Toast.makeText(TestLedMainActivity.this, "click group" + groupPosition,
                        Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
