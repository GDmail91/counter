package com.example.administrator.counter;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015-11-01.
 */
public class Stopwatch extends Activity {

    Chronometer cm;
    TextView miliTextView;
    LinearLayout resultLayout;
    int num = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);
        cm = (Chronometer) findViewById(R.id.chronometer1);
        miliTextView = (TextView) findViewById(R.id.textView1);
        resultLayout = (LinearLayout)findViewById(R.id.linearLayout1);

        final ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton1);



        
        tb.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        if(tb.isChecked()){
                            cm.setBase(SystemClock.elapsedRealtime());
                            cm.start();
                            num = 1;
                        }
                        else{
                            cm.stop();
                        }
                    }
                });
        findViewById(R.id.marking).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                       markRecord();
                    }
                });
        findViewById(R.id.reset).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        cm.setBase(SystemClock.elapsedRealtime());
                        resultLayout.removeAllViews();
                        num = 1;
                    }
                });

        miliTextView.setText("");

        cm.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            public void onChronometerTick(Chronometer chronometer){
                long elapsedMillis = SystemClock.elapsedRealtime() - cm.getBase();
                DecimalFormat numFormatter = new DecimalFormat("###,###");
//                String output = numFormatter.format(elapsedMillis);
//                miliTextView.setText("밀리초:"+output);
            }
        });
    }

    public void start_stopwatch(){
        cm.setBase(SystemClock.elapsedRealtime());
        cm.start();
        num = 1;
    }


    private void markRecord(){
        long elapsedMillis = SystemClock.elapsedRealtime() - cm.getBase();
        long seconds = ((int) elapsedMillis/1000) * 1000;

        TextView recordView = new TextView(this);
        recordView.setTextAppearance(
                getApplicationContext(),
                android.R.style.TextAppearance_Large);

        recordView.setText("["+num+"]"+ cm.getText()+ " " + (elapsedMillis-seconds));
        resultLayout.addView(recordView);
        num++;
    }

}
