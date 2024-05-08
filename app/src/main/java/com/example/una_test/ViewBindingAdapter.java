package com.example.una_test;

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
    public static void setClickNum(@NonNull TextView textView, int clickNum) {
        String oldClick = textView.getText().toString();
        String clickText = String.valueOf(clickNum);
        if (!oldClick.equals(clickText)) {
            textView.setText(clickText);
        }
    }

    @InverseBindingAdapter(attribute = "clickNum", event = "app:clickNumAttrChanged")
    public static int getClickNum(@NonNull TextView textView) {
        return Integer.parseInt(textView.getText().toString());
    }

    // todo: choco 應該是測試用而已, 正式不會把複雜邏輯寫在這, 會不通用
    @BindingAdapter(value = "app:clickNumAttrChanged")
    public static void setClickNumAttrChanged(@NonNull TextView textView,
                                              InverseBindingListener listener) {
        if (listener == null) {
            textView.setOnClickListener(null);
        } else {
            textView.setOnClickListener(v -> {
                int click = Integer.parseInt(textView.getText().toString()) + 1;
                textView.setText(String.valueOf(click));
                listener.onChange();
            });
        }
    }
}
