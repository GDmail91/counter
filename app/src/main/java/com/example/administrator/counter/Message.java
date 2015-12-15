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
 * Created by YS on 2015-12-10.
 */
public class Message extends Activity {
    private static final String TAG = "Message";

    private EditText mTitleText;
    private TextView mTitle_reset;
    private EditText mEditText;
    private TextView mMessage;
    private Button mButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);

        String flag = getIntent().getStringExtra("flag");
        Log.d(TAG, "flag값: " + flag);
        switch (flag) {
            case "set":
                Log.d(TAG, "set");
                setContentView(R.layout.message_input);

                mTitleText = (EditText)findViewById(R.id.title);

                mEditText = (EditText)findViewById(R.id.input_message);
                mButton = (Button)findViewById(R.id.submit_message);

                break;
            case "reset":
                Log.d(TAG, "reset");
                setContentView(R.layout.message_reset);

                mTitle_reset = (TextView)findViewById(R.id.title);
                mMessage = (TextView)findViewById(R.id.input_message);

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
                                mMessage.setText(data.getJSONObject("info").getString("content"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
        }
    }

    // 메세지 등록 버튼 클릭
    public void submit_message(View v) {
        // 메세지 등록된것 서버로 보냄

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "6")
                    // TODO 버튼리스트에서 등록된 버튼 MAC 가져와야함
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", mTitleText.getText())
                    .put("content", mEditText.getText());
            Log.d(TAG, "보내는 데이터: "+jobj.toString());

            new HttpHandler().regFunc(jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    try {
                        if (result.getBoolean("status")) {
                            Log.d(TAG, "메세지 등록됨");
                            Toast toast = Toast.makeText(getApplicationContext(), "카운트 값 초기화", Toast.LENGTH_LONG);
                            toast.show();

                            Intent intent = new Intent(Message.this, ButtonList.class);
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
        Intent intent = new Intent(Message.this, ButtonRegPage.class);
        intent.putExtra("mac_addr", getIntent().getStringExtra("mac_addr"));

        startActivity(intent);
    }
}
