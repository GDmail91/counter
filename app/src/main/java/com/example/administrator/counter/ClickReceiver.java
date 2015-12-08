package com.example.administrator.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2015-12-08.
 */
public class ClickReceiver extends BroadcastReceiver {
    private static final String TAG = "ClickReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("com.example.administrator.CLICK_BUTTON")) {
            String macAddr = intent.getStringExtra("MAC_ADDR");

            // TODO 내부 브로드캐스트 리시버로 받았으니 서버와 동기화하는 작업 필요
            // TODO 여기서 모든 작업이 이루어지게 했지만 이렇게 하는것보다 콜백으로 받아온 result를
            // TODO 다른 액티비티를 실행하여 넘기는것이 좋다.
            // TODO 왜냐하면 이 클래스는 "통신" 클래스 이기 때문

            // 디버깅용 토스트
            Toast.makeText(context, "Broadcast : "+macAddr, Toast.LENGTH_LONG).show();

            // 버튼정보 가져오는 통신 실행
            new HttpHandler().getBtn(macAddr, new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {

                    Log.d(TAG, "버튼정보 콜백 받음");
                    try {
                        Boolean status = result.getBoolean("status");
                        String message = result.getString("message");
                        // 결과에 따라서 인텐트 생성, 액티비티실행
                        if (status) {
                            Log.d(TAG, "버튼정보 가져옴");
                            JSONObject data = result.getJSONObject("data");
                            // TODO 버튼 종류에 따른 기능별 작업 실행
                            switch (data.getString("title")) {
                                case "카운터":
                                    Log.d(TAG, data.getString("info"));
                                    break;
                                case "스톱워치":
                                    break;

                            }

                        } else {
                            Log.d(TAG, "버튼정보 가져오기 실패: "+message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
