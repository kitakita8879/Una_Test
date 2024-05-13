package com.example.una_test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.una_test.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class FragmentPage3 extends Fragment {
    private final List<String> tabTitle = Arrays.asList("tab1", "tab2", "tab3", "tab4",
            "tab5", "tab6", "tab7", "tab8");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tab);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager2);
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(requireActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(0);
        if (savedInstanceState == null) {
            new TabLayoutMediator(tabLayout, viewPager2,
                    (tab, position) -> tab.setText(tabTitle.get(position))).attach();
        }
    }

    private class MyFragmentStateAdapter extends FragmentStateAdapter {
        public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                case 4:
                    return new FragmentPage1();
                case 1:
                case 5:
                    return new FragmentPage2();
                case 2:
                case 6:
                    return new FragmentPage4();
                default:
                    return new FragmentPage5();
            }
        }

        @Override
        public int getItemCount() {
            return tabTitle.size();
        }
    }
}
