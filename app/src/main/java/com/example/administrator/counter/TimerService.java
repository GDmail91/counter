package com.example.administrator.counter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2015-12-08.
 */
public class TimerService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
