package com.example.una_test.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.una_test.R;
import com.google.android.material.snackbar.Snackbar;

public class FragmentPage2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.coordinator);
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                    "Floating Action Button click", Snackbar.LENGTH_SHORT);
            snackbar.setAction("CANCEL", v1 -> snackbar.dismiss())
                    .setBackgroundTint(getResources().getColor(R.color.blue1,
                            requireContext().getTheme()))
                    .setTextColor(Color.WHITE)
                    .show();
        });
    }
}
