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
        setContentView(R.layout.check);

        mTitleText = (EditText) findViewById(R.id.title);
        mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.submit_count);
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
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}