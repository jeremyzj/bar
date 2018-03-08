package com.barmall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.barmall.widget.BottomBar;
import com.facebook.react.ReactActivity;

import org.jetbrains.annotations.NotNull;

//public class MainActivity extends ReactActivity {
//
//    /**
//     * Returns the name of the main component registered from JavaScript.
//     * This is used to schedule rendering of the component.
//     */
//    @Override
//    protected String getMainComponentName() {
//        return "barMall";
//    }
//}

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReactFragment()).commit();

        BottomBar bottomBar = findViewById(R.id.bottom_bar);

        bottomBar.setListener(new BottomBar.Listener() {
            @Override
            public void onTabSelected(@NotNull BottomBar.Tab tab) {
                Log.d(TAG, tab.name());
            }
        });
    }
}
