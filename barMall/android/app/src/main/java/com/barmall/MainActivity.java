package com.barmall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.barmall.fragment.HomeFragment;
import com.barmall.widget.BottomBar;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        Fragment fragment = HomeFragment.Companion.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        BottomBar bottomBar = findViewById(R.id.bottom_bar);

        bottomBar.setListener(new BottomBar.Listener() {
            @Override
            public void onTabSelected(@NotNull BottomBar.Tab tab) {
                Log.d(TAG, tab.name());
            }
        });
    }
}
