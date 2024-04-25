package com.example.una_test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestLedMainActivity extends AppCompatActivity {
    List<GroupItem> group = Arrays.asList(new GroupItem("Accelerating", R.drawable.aecelerating),
            new GroupItem("Constant Speed", R.drawable.constant),
            new GroupItem("Decelerating", R.drawable.decelerating));

    List<ChildItem> battleModeChild = Collections.singletonList(new ChildItem(1, 1, 10, 10));
    List<ChildItem> customizationChild = Collections.singletonList(new ChildItem(1, 1, 10, 10));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_led_main);

        ConstraintLayout battleModeBtn = findViewById(R.id.battle_mode_btn);
        ConstraintLayout customizationBtn = findViewById(R.id.customization_btn);
        ConstraintLayout battleModeItem = findViewById(R.id.battle_mode_item);
        ExpandableListView battleModeView = findViewById(R.id.battle_mode_list_view);
        ExpandableListView customizationItem = findViewById(R.id.customization_list_view);

        ExpandableAdapter battleModeAdapter = new ExpandableAdapter(this, group,
                battleModeChild, false);
        battleModeView.setAdapter(battleModeAdapter);

        battleModeBtn.setOnClickListener(v -> {
            battleModeItem.setVisibility(View.VISIBLE);
            customizationItem.setVisibility(View.INVISIBLE);
            battleModeBtn.getBackground().setTint(getResources().getColor(R.color.black2, getTheme()));
            customizationBtn.getBackground().setTint(getResources().getColor(R.color.black, getTheme()));
        });

        customizationBtn.setOnClickListener(v -> {
            battleModeItem.setVisibility(View.INVISIBLE);
            ExpandableAdapter customizationAdapter = new ExpandableAdapter(this, group,
                    customizationChild, true);
            customizationItem.setAdapter(customizationAdapter);
            customizationItem.setVisibility(View.VISIBLE);
            battleModeBtn.getBackground().setTint(getResources().getColor(R.color.black, getTheme()));
            customizationBtn.getBackground().setTint(getResources().getColor(R.color.black2, getTheme()));
        });

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
        private final int mLedMode, mColor, mBrightness, mSpeed;

        ChildItem(int ledMode, int color, int brightness, int speed) {
            this.mLedMode = ledMode;
            this.mBrightness = brightness;
            this.mColor = color;
            this.mSpeed = speed;
        }
    }

    private static class ExpandableAdapter extends BaseExpandableListAdapter {
        List<GroupItem> group;
        List<ChildItem> childItem;
        LayoutInflater layoutInflater;
        ImageView imgIndicator, imgBattleColor;
        boolean isCustomization;
        ConstraintLayout brightnessItem, customLed, customColor, ledItem, colorItem, speedItem;
        TextView txtBattleLed;
        SeekBar speedBar, brightnessBar;

        ExpandableAdapter(Context context, List<GroupItem> group, List<ChildItem> childItem, boolean isCustomization) {
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
            return childItem.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
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

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.expandable_list_view_item, parent, false);
            }
            convertView.setTag(R.layout.expandable_list_view_item, groupPosition);
            TextView txt_title = convertView.findViewById(R.id.txt_title);
            ImageView img_title = convertView.findViewById(R.id.img_title);
            txt_title.setText(group.get(groupPosition).mode);
            img_title.setImageResource(group.get(groupPosition).img);
            imgIndicator = convertView.findViewById(R.id.indicator);
            imgIndicator.setSelected(false);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.expandable_list_view_childitem, parent, false);
            }
            convertView.setTag(R.layout.expandable_list_view_childitem, groupPosition);
            imgIndicator.setSelected(true);
            brightnessItem = convertView.findViewById(R.id.brightness_item);
            customLed = convertView.findViewById(R.id.customization_led);
            customColor = convertView.findViewById(R.id.customization_color);
            imgBattleColor = convertView.findViewById(R.id.img_color);
            txtBattleLed = convertView.findViewById(R.id.txt_battle_led);
            speedItem = convertView.findViewById(R.id.speed_item);
            speedBar = convertView.findViewById(R.id.seek_bar_speed);
            ledItem = convertView.findViewById(R.id.led_mode);
            colorItem = convertView.findViewById(R.id.color_item);
            brightnessBar = convertView.findViewById(R.id.seek_bar_brightness);
            if (isCustomization) {
                brightnessItem.setVisibility(View.VISIBLE);
                customLed.setVisibility(View.VISIBLE);
                customColor.setVisibility(View.VISIBLE);
                imgBattleColor.setVisibility(View.INVISIBLE);
                txtBattleLed.setVisibility(View.INVISIBLE);
                speedItem.setEnabled(true);
                ledItem.setEnabled(true);
                colorItem.setEnabled(true);
            } else {
                brightnessItem.setVisibility(View.GONE);
                customLed.setVisibility(View.INVISIBLE);
                customColor.setVisibility(View.INVISIBLE);
                imgBattleColor.setVisibility(View.VISIBLE);
                txtBattleLed.setVisibility(View.VISIBLE);
                speedItem.setEnabled(false);
                speedBar.setEnabled(false);
                ledItem.setEnabled(false);
                colorItem.setEnabled(false);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
