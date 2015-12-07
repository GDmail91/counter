package com.example.administrator.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by YS on 2015-12-08.
 */
public class ClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("com.example.administrator.CLICK_BUTTON")) {
            String macAddr = intent.getStringExtra("MAC_ADDR");

            Toast.makeText(context, "Broadcast : "+macAddr, Toast.LENGTH_LONG).show();
        }
    }
}
