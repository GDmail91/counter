package com.example.administrator.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2015-12-12.
 */
public class Check extends Activity {
    private static final String TAG = "Count";

    private EditText mTitleText;
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String flag = getIntent().getStringExtra("flag");
        Log.d(TAG, "flag값: "+flag);
        switch (flag) {
            case "set":
                Log.d(TAG, "set");
                setContentView(R.layout.check);

                mTitleText = (EditText)findViewById(R.id.title);
                mEditText = (EditText) findViewById(R.id.editText);
                mButton = (Button)findViewById(R.id.submit_count);

                break;
            case "reset":
                Log.d(TAG, "reset");
                setContentView(R.layout.check_reset);

                mTitleText = (EditText)findViewById(R.id.title);
                mEditText = (EditText) findViewById(R.id.editText);
                mButton = (Button)findViewById(R.id.reset_count);

                break;
        }
    }

    public void submit_count(View v) {
        int check = Integer.valueOf(mEditText.getText().toString());
        Log.d(TAG, "카운트 수: " + check);
        Log.d(TAG, "MAC_ADDR: " + getIntent().getStringExtra("mac_addr"));

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "4")
                            // TODO 버튼리스트에서 등록된 버튼 MAC 가져와야함
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", mTitleText.getText().toString())
                    .put("chk", check);

            new HttpHandler().regFunc(jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    Log.d(TAG, "카운터 버튼 등록됨");
                    Toast toast = Toast.makeText(getApplicationContext(), "카운터버튼 등록", Toast.LENGTH_LONG);
                    toast.show();

                    Intent intent = new Intent(Check.this, ButtonList.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 체크 리셋
     * @param v
     */
    public void reset_count(View v) {
        String title = mTitleText.getText().toString();
        Log.d(TAG, "버튼 타이틀: " + title);
        Log.d(TAG, "MAC_ADDR: " + getIntent().getStringExtra("mac_addr"));

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "4")
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", title);

            new HttpHandler().resetFunc("check", jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    try {
                        if (result.getBoolean("status")) {
                            Log.d(TAG, "체크 값 초기화");
                            Toast toast = Toast.makeText(getApplicationContext(), "체크 값 초기화", Toast.LENGTH_LONG);
                            toast.show();

                            Intent intent = new Intent(Check.this, ButtonList.class);
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
        Intent intent = new Intent(Check.this, MainActivity.class);
        intent.putExtra("mac_addr", getIntent().getStringExtra("mac_addr"));

        startActivity(intent);
    }
}