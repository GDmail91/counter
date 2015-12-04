package com.example.administrator.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by YS on 2015-12-03.
 */
public class MySchedulerListener extends BroadcastReceiver{

    private static final String TAG = "MySchedulerListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();

        if (name.equals("com.example.administrator.counter.schedule")) {
            Toast.makeText(context, "머시기", Toast.LENGTH_SHORT).show();
        }
    }
}
