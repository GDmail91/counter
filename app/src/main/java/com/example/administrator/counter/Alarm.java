package com.example.administrator.counter;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;

import android.app.Activity;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.app.PendingIntent;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015-12-05.
 */






public class Alarm extends Activity {

    static final int TIME_START = 0;
    static final int TIME_END = 1;
    static final int TIME_SHOW = 2;
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_button);
        mText = (TextView)findViewById(R.id.text);
        start_alarm = (Button) findViewById(R.id.start_alarm);
        end_alarm = (Button) findViewById(R.id.end_alarm);
        show_alarm = (Button)findViewById(R.id.show_alarm);

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
    private void updateDisplay(){
        mText.setText(String.format("%d시 %d분에서 %d시 %d분",h1,m1,h2,m2));
    }

    protected  Dialog onCreateDialog(int id){
        switch(id){
            case TIME_START:
                return new TimePickerDialog(this,sTimeSetListener, mHour,mMinute,false);
            case TIME_END:
                return new TimePickerDialog(this,eTimeSetListener, mHour,mMinute,false);
            case TIME_SHOW:
                updateDisplay();
        }
        return null;

    }

};
