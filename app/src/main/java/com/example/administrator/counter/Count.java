package com.example.administrator.counter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-18.
 */
public class Count extends Activity {
    private static final String TAG = "Count";

    private EditText mTitle;
    private TextView mTitle_reset;
    private TextView mCount;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String flag = getIntent().getStringExtra("flag");
        Log.d(TAG, "flag값: "+flag);
        switch (flag) {
            case "set":
                Log.d(TAG, "set");
                setContentView(R.layout.count);

                mTitle = (EditText)findViewById(R.id.title);
                mButton = (Button)findViewById(R.id.submit_count);

                break;
            case "reset":
                Log.d(TAG, "reset");
                setContentView(R.layout.count_reset);

                mTitle_reset = (TextView)findViewById(R.id.title);
                mCount = (TextView)findViewById(R.id.count);
                mButton = (Button)findViewById(R.id.reset_count);

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
                                mCount.setText(data.getJSONObject("info").getString("cnt"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
        }
    }

    /**
     * 카운트 등록
     * @param v
     */
    public void submit_count(View v) {
        String title = mTitle.getText().toString();
        Log.d(TAG, "버튼 타이틀: " + title);
        Log.d(TAG, "MAC_ADDR: " + getIntent().getStringExtra("mac_addr"));

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "1")
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", title);

            new HttpHandler().regFunc(jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    try {
                        if (result.getBoolean("status")) {
                            Log.d(TAG, "카운트 버튼 등록됨");
                            Toast toast = Toast.makeText(getApplicationContext(), "카운트 버튼 등록됨", Toast.LENGTH_LONG);
                            toast.show();

                            Intent intent = new Intent(Count.this, ButtonList.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "카운트 버튼 등록 실패");
                            Toast toast = Toast.makeText(getApplicationContext(), "카운트 버튼 등록 실패", Toast.LENGTH_LONG);
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

    /**
     * 카운트 리셋
     * @param v
     */
    public void reset_count(View v) {
        String title = mTitle_reset.getText().toString();
        Log.d(TAG, "버튼 타이틀: " + title);
        Log.d(TAG, "MAC_ADDR: " + getIntent().getStringExtra("mac_addr"));

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "1")
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", title);

            new HttpHandler().resetFunc("count", jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    try {
                        if (result.getBoolean("status")) {
                            Log.d(TAG, "카운값 초기화");
                            Toast toast = Toast.makeText(getApplicationContext(), "카운트 값 초기화", Toast.LENGTH_LONG);
                            toast.show();

                            Intent intent = new Intent(Count.this, ButtonList.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "카운값 초기화 실패");
                            Toast toast = Toast.makeText(getApplicationContext(), "카운트 값 초기화 실패", Toast.LENGTH_LONG);
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

    /**
     * 기능 재설정 버튼
     * @param v
     */
    public void resetFunc(View v) {
        Intent intent = new Intent(Count.this, ButtonRegPage.class);
        intent.putExtra("mac_addr", getIntent().getStringExtra("mac_addr"));

        startActivity(intent);
    }
}
