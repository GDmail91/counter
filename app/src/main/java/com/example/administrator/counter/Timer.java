package com.example.administrator.counter;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Context;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.R.color;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2015-12-08.
 */
public class Timer extends Activity{
    AlarmService mAlarmService = null;
    int i = -1;
    private EditText edtTimerValue;
    private Button stopTime;
    private Button setTime;
    private Button startTime;
    private TextView textViewShowTime;
    private CountDownTimer countDownTimer;

    private long totalTimeCountInMilliseconds;
    private long timeBlinkInMilliseconds;//
    private boolean blink;// on and off




    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected( ComponentName name,
                                        IBinder service )
        {
            Log.d("superdroid", "onServiceConnected()");
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mAlarmService = binder.getAlarmService();
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            Log.d( "superdroid", "onServiceDisconnected()" );
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        Log.d("superdroid","set Timer");
        edtTimerValue = (EditText)findViewById(R.id.edtTimerValue);
        setTime = (Button)findViewById(R.id.setTime);
        startTime = (Button)findViewById(R.id.startTime);
        stopTime = (Button)findViewById(R.id.stopTime);
        textViewShowTime = (TextView)findViewById(R.id.timeCount);


    }

    public void onClick(View v){


        if (v.getId() ==  R.id.startTime){
            textViewShowTime.setText("start!");
            setTimer();

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtTimerValue.getWindowToken(),0);

            stopTime.setVisibility(View.VISIBLE);
            startTime.setVisibility(View.GONE);
            edtTimerValue.setVisibility(View.GONE);
            edtTimerValue.setText("");
            startTimer();

         if (v.getId() == R.id.setTime){
             int time = Integer.parseInt(edtTimerValue.getText().toString());
             Log.d("superdroid","timer test");
             Log.d("superdroid",String.valueOf(time));
         }
        }
    }


    private void setTimer(){
        int time = 0;
        if (edtTimerValue.getText().toString().equals("")){
            time = Integer.parseInt(edtTimerValue.getText().toString());
            Log.d("superdroid",String.valueOf(time));
        }
        else
            Toast.makeText(this,"please Enter Minutes...",Toast.LENGTH_LONG).show();

            totalTimeCountInMilliseconds = 60 * time * 1000;
            timeBlinkInMilliseconds = 30 * 1000;

    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds,500){
            public void onTick(long leftTimeInMilliseconds){
                long seconds = leftTimeInMilliseconds/1000;

                textViewShowTime.setTextAppearance(getApplicationContext(),R.style.TextAppearance_AppCompat_Medium);
                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                    textViewShowTime.setTextAppearance(getApplicationContext(),
                            R.style.Base_AlertDialog_AppCompat_Light);
                    // change the style of the textview .. giving a red
                    // alert style

                    if (blink) {
                        textViewShowTime.setVisibility(View.VISIBLE);
                        // if blink is true, textview will be visible
                    } else {
                        textViewShowTime.setVisibility(View.INVISIBLE);
                    }

                    blink = !blink; // toggle the value of blink
                }
                textViewShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }

            public void onFinish(){
                textViewShowTime.setText("Time ul!");
                textViewShowTime.setVisibility(View.VISIBLE);
                textViewShowTime.setVisibility(View.VISIBLE);
                textViewShowTime.setVisibility(View.GONE);
                textViewShowTime.setVisibility(View.VISIBLE);
            }
        }.start();
    }
};
