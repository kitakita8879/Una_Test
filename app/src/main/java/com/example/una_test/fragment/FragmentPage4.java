package com.example.una_test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.una_test.R;

public class FragmentPage4 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_popup).setOnClickListener(this::showPopupWindow);
    }

    private void showPopupWindow(View v) {
        PopupWindow popupWindow;
        View popView = getLayoutInflater().inflate(
                R.layout.test_2_mode_select_dialog, null, false);

        popupWindow = new PopupWindow(popView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(v, 0, 30);

        popView.findViewById(R.id.view_mode_1).setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "mode1", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popView.findViewById(R.id.view_mode_2).setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "mode2", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popView.findViewById(R.id.view_mode_3).setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "mode3", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }
}
