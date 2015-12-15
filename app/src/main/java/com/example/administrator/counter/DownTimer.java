package com.example.administrator.counter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;

/**
 * Created by Administrator on 2015-12-12.
 */
public class DownTimer extends Activity {
    private static final String TAG = "DownTimer";

    private Thread countDownThread;

    private EditText mTitle;
    private TextView mTitle_reset;
    private Button set_Time;
    private EditText edit_Time;
    private TextView mTime_reset;
    private int countdown;
    private int number;

    private CountDownTimer cdt;
    private boolean done_flag = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String flag = getIntent().getStringExtra("flag");
        Log.d(TAG, "flag값: " + flag);
        switch (flag) {
            case "set":
                Log.d(TAG, "set");
                setContentView(R.layout.down_timer);

                mTitle = (EditText) findViewById(R.id.title);

                edit_Time = (EditText) findViewById(R.id.edit_time);
                set_Time = (Button) findViewById(R.id.set_Time);

                break;
            case "reset":
                Log.d(TAG, "reset");
                setContentView(R.layout.timer_reset);

                mTitle_reset = (TextView) findViewById(R.id.title);

                mTime_reset = (TextView) findViewById(R.id.edit_time);

                new HttpHandler().getBtn(getIntent().getStringExtra("mac_addr"), this, new ReceiverCallback() {
                    @Override
                    public void httpProcessing(Context context, JSONObject result) {
                        Log.d(TAG, "버튼정보 콜백 받음");
                        try {
                            Boolean status = result.getBoolean("status");
                            String message = result.getString("message");
                            // 결과에 따라서 인텐트 생성, 액티비티실행
                            if (status) {
                                Log.d(TAG, "버튼정보 가져옴");
                                //getMessage(context);

                                JSONObject data = new JSONObject(result.getString("data"));
                                mTitle_reset.setText(data.getString("title")); // 타이틀 가져옴
                                Log.d(TAG, new Time(Long.valueOf(data.getJSONObject("info").getString("time"))).toString());
                                mTime_reset.setText(data.getJSONObject("info").getString("time")+"/"+data.getJSONObject("info").getString("time_max")); // 현재시간 가져옴
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_Time: {
                countdown = Integer.valueOf(edit_Time.getText().toString()) * 60 * 1000;
                Log.d("superdroid", String.valueOf(countdown));

                try {
                    JSONObject jobj = new JSONObject()
                            .put("fid", "5")
                            .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                            .put("title", mTitle.getText().toString())
                            .put("time", Integer.toString(countdown));

                    new HttpHandler().regFunc(jobj.toString(), new MyCallback() {
                        @Override
                        public void httpProcessing(JSONObject result) {
                            try {
                                if (result.getBoolean("status")) {
                                    Log.d(TAG, "타이머 버튼 등록됨");
                                    Toast toast = Toast.makeText(getApplicationContext(), "타이머 버튼 등록됨", Toast.LENGTH_LONG);
                                    toast.show();

                                    Intent intent = new Intent(DownTimer.this, ButtonList.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, "타이머 버튼 등록 실패");
                                    Toast toast = Toast.makeText(getApplicationContext(), "타이머 버튼 등록 실패", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopcountdown() {
        countDownThread.interrupt();
    }

    public void startcountdown(final String mac_addr, int n) {
        number = n;
        done_flag = false;
        // 카운트다운 타이머 (첫번째 인자는 실행할 초, 두번째 인자는 실행할 초단위
        cdt = new CountDownTimer(n, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                number--;
            }

            @Override
            public void onFinish() {
                done_flag = true;
                Log.d(TAG, "타이머 끝남");
                try {
                    JSONObject jobj = new JSONObject()
                            .put("mac_addr", mac_addr);
                    new HttpHandler().resetFunc("timer", jobj.toString(), new MyCallback() {
                        @Override
                        public void httpProcessing(JSONObject result) {
                            try {
                                if (result.getBoolean("status")) {
                                    Log.d(TAG, "타이머 버튼 종료");
                                } else {
                                    Log.d(TAG, "타이머 버튼 통신 실패");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        cdt.start();

/*
        if (countDownThread == null)
        {
            countDownThread = new Thread("Count Thread") {
                public void run() {
                    while (true) {
                        number -- ;

                        try {
                            Log.d(TAG, "타이머 count: "+number);
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            };

            countDownThread.start();
        }*/
    }

    public int killcountdown(String mac_addr) {
        if (!done_flag) {
            cdt.cancel();

            try {
                Log.d(TAG, "타이머 종료");
                Log.d(TAG, "남은시간: " + number);
                JSONObject jobj = new JSONObject()
                        .put("time", Integer.toString(number))
                        .put("mac_addr", mac_addr);
                new HttpHandler().putFunc("timer", jobj.toString(), new MyCallback() {
                    @Override
                    public void httpProcessing(JSONObject result) {
                        try {
                            if (result.getBoolean("status")) {
                                Log.d(TAG, "타이머 버튼 종료");
                            } else {
                                Log.d(TAG, "타이머 버튼 통신 실패");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "타이머가 이미 종료되었음");
        }
        return number;
    }


    /**
     * 기능 재설정 버튼
     *
     * @param v
     */
    public void resetFunc(View v) {
        Intent intent = new Intent(DownTimer.this, ButtonRegPage.class);
        intent.putExtra("mac_addr", getIntent().getStringExtra("mac_addr"));

        startActivity(intent);
    }

}
