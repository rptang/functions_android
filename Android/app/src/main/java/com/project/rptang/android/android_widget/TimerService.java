package com.project.rptang.android.android_widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.project.rptang.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private Timer timer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public TimerService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        }, 0, 1000);
    }

    private void updateViews(){
        String time = sdf.format(new Date());
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget);
        rv.setTextViewText(R.id.tv_time,time);
        AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(),WidgetProvider.class);
        awm.updateAppWidget(cn,rv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }
}
