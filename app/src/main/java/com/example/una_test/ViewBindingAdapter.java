package com.example.una_test;

import static java.lang.Integer.parseInt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class ViewBindingAdapter {
    @BindingAdapter("android:selected")
    public static void setSelected(@NonNull View view, boolean selected) {
        if (view.isSelected() != selected) {
            view.setSelected(selected);
        }
    }

    @BindingAdapter("clickNum")
    public static void setClickNum(TextView textView, int clickNum) {
        String oldClick = textView.getText().toString();
        if (!oldClick.equals(String.valueOf(clickNum))) {
            textView.setText(String.valueOf(clickNum));
        }
    }

    @InverseBindingAdapter(attribute = "clickNum", event = "app:clickNumAttrChanged")
    public static int getClickNum(TextView textView) {
        return parseInt(textView.getText().toString());
    }

    @BindingAdapter(value = "app:clickNumAttrChanged")
    public static void setClickNumAttrChanged(TextView textView,
                                              final InverseBindingListener listener) {
        if (listener == null) {
            textView.setOnClickListener(null);
        } else {
            textView.setOnClickListener(v -> {
                int click = parseInt(textView.getText().toString()) + 1;
                textView.setText(String.valueOf(click));
                listener.onChange();
            });
        }
    }
}
