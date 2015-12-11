package com.example.administrator.counter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2015-12-10.
 */
public class Message extends Activity {
    private static final String TAG = "Message";

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.message_input);

        mEditText = (EditText)findViewById(R.id.input_message);
        mButton = (Button)findViewById(R.id.submit_message);
    }

    // 메세지 등록 버튼 클릭
    public void submit_message(View v) {
        // 메세지 등록된것 서버로 보냄

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "6")
                    // TODO 버튼리스트에서 등록된 버튼 MAC 가져와야함
                    .put("mac_addr", "TESTMACADDR5")
                    .put("title", "테스트2")
                    .put("content", mEditText.getText());
            Log.d(TAG, "보내는 데이터: "+jobj.toString());

            new HttpHandler().regFunc(jobj.toString(), new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    Log.d(TAG, "메세지 버튼 등록됨");
                    Toast toast = Toast.makeText(getApplicationContext(), "메세지버튼 등록", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
