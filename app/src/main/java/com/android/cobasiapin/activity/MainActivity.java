package com.android.cobasiapin.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.android.cobasiapin.R;
import com.android.cobasiapin.fragment.BerandaFragment;
import com.android.cobasiapin.fragment.KelolaFragment;
import com.android.cobasiapin.fragment.AiFragment;
import com.android.cobasiapin.fragment.ChatFragment;
import com.android.cobasiapin.fragment.AkunFragment;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Load fragment pertama
        loadFragment(new BerandaFragment());

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_beranda) {
                fragment = new BerandaFragment();
            } else if (id == R.id.nav_kelola) {
                fragment = new KelolaFragment();
            } else if (id == R.id.nav_ai) {
                fragment = new AiFragment();
            } else if (id == R.id.nav_chat) {
                fragment = new ChatFragment();
            } else if (id == R.id.nav_akun) {
                fragment = new AkunFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}