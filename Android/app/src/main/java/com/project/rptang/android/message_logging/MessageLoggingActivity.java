package com.project.rptang.android.message_logging;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.project.rptang.android.R;

public class MessageLoggingActivity extends Activity {

    private static final String TAG = "MessageLoggingActivity";

    private MyActionBar actionBar;
    private DisplayMetrics mDisplayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logging);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Log.d(TAG, "onCreate: "+mDisplayMetrics.density);

        actionBar = (MyActionBar) findViewById(R.id.message_action_bar);

        actionBar.setOnMyActionbarClickListener(new MyActionBar.MyActionbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(MessageLoggingActivity.this, "left button", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void rightClick() {
                Toast.makeText(MessageLoggingActivity.this, "right button", Toast.LENGTH_SHORT).show();
            }
        });

        actionBar.setRightIsVisible(true);
    }
}
