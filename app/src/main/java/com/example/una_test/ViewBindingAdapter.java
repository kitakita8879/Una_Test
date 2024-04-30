package com.example.una_test;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class ViewBindingAdapter {
    @BindingAdapter("android:selected")
    public static void setSelected(@NonNull View view, boolean selected) {
        view.setSelected(selected);
    }
}
