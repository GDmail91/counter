package com.example.administrator.counter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2015-12-12.
 */
public class DownTimer extends Activity {

    private Thread countDownThread;
    private Button set_Time;
    private EditText edit_Time;
    private int countdown;
    private int number;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_timer);
        edit_Time = (EditText)findViewById(R.id.edit_Time);
        set_Time = (Button)findViewById(R.id.set_Time);



    }
    public void onClick( View v )
    {
        switch( v.getId() )
        {
            case R.id.set_Time:
            {
                countdown = Integer.valueOf(edit_Time.getText().toString())*60*1000;
                Log.d("superdroid",String.valueOf(countdown));

            }
        }
    }

    public void stopcountdown(){
        countDownThread.interrupt();
    }

    public void startcountdown(int n){

        number = n;
        if (countDownThread == null)
        {
            countDownThread = new Thread("Count Thread") {
                public void run() {
                    while (true) {
                        number -- ;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        ;
                    }
                }
            };

            countDownThread.start();
        }
    }
    public int killcountdown(){
        countDownThread.stop();
        return number;
    }




}
