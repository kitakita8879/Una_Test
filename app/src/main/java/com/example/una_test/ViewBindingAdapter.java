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
        // todo: choco 建議養成好習慣, 避免重複判斷
        if (!oldClick.equals(String.valueOf(clickNum))) {
            textView.setText(String.valueOf(clickNum));
        }
    }

    // todo: choco 建議養成新增 @NonNull @Nullable 提示, 培養非空意識
    @InverseBindingAdapter(attribute = "clickNum", event = "app:clickNumAttrChanged")
    public static int getClickNum(TextView textView) {
        return parseInt(textView.getText().toString());
    }

    // todo: choco final 在支援 lambda 後已不需要, 可以統一新增或統一省略, 保持一致性, 可研究 coding style
    // todo: choco 應該是測試用而已, 正式不會把複雜邏輯寫在這, 會不通用
    @BindingAdapter(value = "app:clickNumAttrChanged")
    public static void setClickNumAttrChanged(TextView textView,
                                              final InverseBindingListener listener) {
        if (listener == null) {
            textView.setOnClickListener(null);
        } else {
            textView.setOnClickListener(v -> {
                // todo: choco 盡量不 import static function 進來
                int click = parseInt(textView.getText().toString()) + 1;
                textView.setText(String.valueOf(click));
                listener.onChange();
            });
        }
    }
}
