package com.example.administrator.counter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2015-12-05.
 */






public class Alarm extends Activity {
    private static final String TAG = "Alarm";

    static Alarm alarm;
    AlarmManager mAlarmManager;
    AlarmService mAlarmService = null;
    static final int TIME_START = 0;
    static final int TIME_END = 1;
    static final int TIME_SHOW = 2;
    static final int TIME_CONNECT = 3;
    private int mHour;
    private int mMinute;
    private int h1;
    private int m1;

    private int h2;
    private int m2;

    private TextView mText;
    private Button start_alarm;
    private Button end_alarm;
    private Button show_alarm;
    private Button connect_alarm;
    private Thread test_Thread = null;
    private int hour1;
    private int minute1;
    private int hour2;
    private int minute2;


    public void getInfo(int ho1,int mi1,int ho2,int mi2){

        hour1 = ho1;
        minute1 = mi1;
        hour2 = ho2;
        minute2 = mi2;
        Log.d("super", "here!!");
        if( test_Thread == null) {
            test_Thread = new Thread("Alarm test_Thread") {
                public void run() {
                    while (true) {

                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        if (hour1>12 &&hour2 < 12) {
                            hour2 = hour2 + 24;
                        }
                        if (mHour < 12) {
                            mHour = mHour + 24;
                            Log.i("superdroid",toString().valueOf(mHour));

                            if (mHour <= hour2 && mHour >= hour1) {
                                Log.i("superdroid", "GOOD ALARAM11!!");
                            }
                        };
                        if (mHour > 12) {
                            Log.i("superdroid", "설정1 : " + hour1);
                            Log.i("superdroid", "설정2 : " + hour2);
                            if (mHour <= hour2 && mHour >= hour1) {
                                Log.i("superdroid", "GOOD ALARAM22!!");
                                break;
                            };
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        };
                    }

                }
            };
            test_Thread.start();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected( ComponentName name,
                                        IBinder service )
        {
            Log.d( "superdroid", "onServiceConnected()" );
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
        setContentView(R.layout.alarm_button);

        alarm = this;
        mText = (TextView)findViewById(R.id.text);
        start_alarm = (Button) findViewById(R.id.start_alarm);
        end_alarm = (Button) findViewById(R.id.end_alarm);
        show_alarm = (Button)findViewById(R.id.show_alarm);
        connect_alarm = (Button)findViewById(R.id.connect_alarm);
        // 알람 관리자 핸들을 구한다
        mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_START);


            }
        });



        end_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDialog(TIME_END);
            }

        });

        show_alarm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                showDialog(TIME_SHOW);
            }
        });

        connect_alarm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                showDialog(TIME_CONNECT);
            }

        });
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


    }


    private TimePickerDialog.OnTimeSetListener sTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    h1 = hourOfDay;
                    m1 = minute;

                }
            };

    private TimePickerDialog.OnTimeSetListener eTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    h2 = hourOfDay;
                    m2 = minute;
                }
            };
    private void connect_service(){
        Toast.makeText( Alarm.this,
                "Cur Count : " +
                        mAlarmService.getCurCountNumber(),
                Toast.LENGTH_LONG ).show();
    };




    private void updateDisplay(){
        mText.setText(String.format("%d시 %d분에서 %d시 %d분", h1, m1, h2, m2));
        onStartAlarm();
        onEndAlarm();

    }

    protected  Dialog onCreateDialog(int id){
        switch(id){
            case TIME_START:
                return new TimePickerDialog(this,sTimeSetListener, mHour,mMinute,false);

            case TIME_END:
                return new TimePickerDialog(this,eTimeSetListener, mHour,mMinute,false);
            case TIME_SHOW:
                updateDisplay();
                break;
            case TIME_CONNECT:
                connect_service();

        }
        return null;

    }

    /**
     * 반복 알람 시작 - 특정 시간 지정
     */

    public void onStartAlarm() {
        Log.d(TAG, "알람 등록");
        // 수행할 동작을 생성
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.example.administrator.ALARM_RING");
        PendingIntent pIntent = PendingIntent.getBroadcast(Alarm.this, 0, intent, 0);


        // 알람이 발생할 정확한 시간을 지정
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);
        calendar.set(year, month, date, h1, m1);

        // 반복 알람 시작
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pIntent);
    }

    /**
     * 반복 알람 시작 - 특정 시간 지정
     */

    public void onEndAlarm() {
        Log.d(TAG, "알람 등록");
        // 수행할 동작을 생성
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.example.administrator.ALARM_END");
        PendingIntent pIntent = PendingIntent.getBroadcast(Alarm.this, 0, intent, 0);


        // 알람이 발생할 정확한 시간을 지정
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);
        calendar.set(year, month, date, h1, m1, 0);

        // 반복 알람 시작
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pIntent);
    }


    /**
     * 알람 중지
     */
    public void onBtnStop() {
        // 수행할 동작을 생성
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(Alarm.this, 0, intent, 0);
        // 알람 중지
        mAlarmManager.cancel(pIntent);
    }

};
