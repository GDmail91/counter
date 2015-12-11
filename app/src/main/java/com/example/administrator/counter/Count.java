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
 * Created by Administrator on 2015-09-18.
 */
public class Count extends Activity {
    private static final String TAG = "Count";

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count);

        mEditText = (EditText)findViewById(R.id.editText);
        mButton = (Button)findViewById(R.id.submit_count);
    }

    public void submit_count(View v) {
        int count = Integer.valueOf(mEditText.getText().toString());
        Log.d(TAG, "카운트 수: " + count);
        Log.d(TAG, "MAC_ADDR: " + getIntent().getStringExtra("mac_addr"));

        try {
            JSONObject jobj = new JSONObject()
                    .put("fid", "1")
                    .put("mac_addr", getIntent().getStringExtra("mac_addr"))
                    .put("title", "테스트3")
                    // TODO 버튼값 안보내줘도됨
                    .put("cnt", count);

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
