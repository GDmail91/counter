package com.example.administrator.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public void onReceive(final Context context, Intent intent) {
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
            new HttpHandler().getBtn(macAddr, context, new ReceiverCallback() {
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
                            JSONObject info;
                            // TODO 버튼 종류에 따른 기능별 작업 실행
                            switch (data.getString("title")) {
                                case "카운터":
                                    Log.d(TAG, data.getString("info"));

                                    break;
                                case "스톱워치":
                                    break;

                                case "테스트" :
                                    Log.d(TAG, "테스트 알람 클릭");
                                    info = new JSONObject(data.getString("info"));
                                    String start = info.getString("start");
                                    String end = info.getString("end");

                                    String[] s = start.split("_");
                                    String[] e = end.split("_");
                                    int h1 = Integer.valueOf(s[0]);
                                    int m1 = Integer.valueOf(s[1]);
                                    int h2 = Integer.valueOf(e[0]);
                                    int m2 = Integer.valueOf(e[1]);

                                    int flag = 1;
                                    new AlarmService().getInfo(h1, m1, h2, m2);
                                    // TODO 위 new AlarmService().getInfo(h1, m1, h2, m2); 의 결과값에 따라서
                                    // TODO flag 설정하여 getMessage에 보내는 메세지를 다르게 해야함
                                    switch (flag) {
                                        case 1:
                                            getMessage(context, "해야할게 있어요");
                                            break;
                                        case 2:
                                            getMessage(context, "수행이 끝났어요");
                                            break;
                                        case 3:
                                            getMessage(context, "할일을 못했어요ㅠ");
                                            break;
                                    }

                                    break;
                                case "테스트2" :
                                    Log.d(TAG, "테스트 알람 클릭");

                                    getMessage(context, result.getString("content"));
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


    // 팝업 메세지 알림
    private void getMessage(Context context, String data) {
        SharedPreferences prefs = context.getSharedPreferences("PrefName", context.MODE_PRIVATE);
        boolean is_popup = prefs.getBoolean("is_login", true);

        Log.d(TAG, "메세지 팝업 함수");
        // 만약 팝업 알림 설정이 켜져있으면 실행한다.
        if (is_popup) {
            Log.d(TAG, "메세지 팝업 올림");
            // 팝업으로 사용할 액티비티를 호출할 인텐트를 작성한다.
            Intent popupIntent = new Intent(context, ShowMessage.class)
                    .putExtra("message", data)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 그리고 호출한다.
            context.startActivity(popupIntent);
        }
    }
}
