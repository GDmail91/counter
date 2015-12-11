package com.example.administrator.counter;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by YS on 2015-12-11.
 */
public interface ReceiverCallback {
    public void httpProcessing(Context context, JSONObject result);
}
