package com.example.una_test;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.una_test.fragment.FragmentPage1;
import com.example.una_test.fragment.FragmentPage2;
import com.example.una_test.fragment.FragmentPage3;
import com.example.una_test.fragment.FragmentPage4;
import com.example.una_test.fragment.FragmentPage5;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class TestFragmentActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private FragmentPage1 mFragmentPage1;
    private FragmentPage2 mFragmentPage2;
    private FragmentPage3 mFragmentPage3;
    private FragmentPage4 mFragmentPage4;
    private FragmentPage5 mFragmentPage5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_main);

        mFragmentManager = getSupportFragmentManager();
        mFragmentPage1 = new FragmentPage1();
        mFragmentPage2 = new FragmentPage2();
        mFragmentPage3 = new FragmentPage3();
        mFragmentPage4 = new FragmentPage4();
        mFragmentPage5 = new FragmentPage5();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        NavigationView navigationView = findViewById(R.id.nav);
        DrawerLayout drawer = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.label_drawer_open, R.string.label_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            replaceFragment(mFragmentPage1);
            navigationView.setCheckedItem(R.id.nav_1);
            bottomNavigationView.setSelectedItemId(R.id.nav_1);
        }

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            selectNavItem(menuItem);
            navigationView.setCheckedItem(menuItem.getItemId());
            return true;
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawer(GravityCompat.START);
            selectNavItem(menuItem);
            bottomNavigationView.setSelectedItemId(menuItem.getItemId());
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

    private void selectNavItem(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_1) {
            if (!mFragmentPage1.isAdded()) {
                replaceFragment(mFragmentPage1);
            }
        } else if (id == R.id.nav_2) {
            if (!mFragmentPage2.isAdded()) {
                replaceFragment(mFragmentPage2);
            }
        } else if (id == R.id.nav_3) {
            if (!mFragmentPage3.isAdded()) {
                replaceFragment(mFragmentPage3);
            }
        } else if (id == R.id.nav_4) {
            if (!mFragmentPage4.isAdded()) {
                replaceFragment(mFragmentPage4);
            }
        } else {
            if (!mFragmentPage5.isAdded()) {
                replaceFragment(mFragmentPage5);
            }
        }
    }
}