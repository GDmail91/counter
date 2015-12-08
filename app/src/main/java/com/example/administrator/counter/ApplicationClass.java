package com.example.administrator.counter;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by 영수 on 2015-10-08.
 */
public class ApplicationClass extends Application {

    private static final String TAG = "ApplicationClass";

    public static ApplicationClass app = new ApplicationClass();

    // 서버에 보낼 데이터
    private JSONObject data;

    // 세션 토큰
    public static String SESSION_TOKEN;

    // GCM 토큰
    public static String GCM_TOKEN;

    @Override
    public void onCreate() {
        // 세션 토큰 설정
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        SESSION_TOKEN = prefs.getString("cookie", "");
        Log.d(TAG, "세션 토큰: "+SESSION_TOKEN);

        // Http Client로 데이터 동기화하는 코드
        // TODO
    }


    public static void setToken(String token) {
        SharedPreferences prefs = app.getSharedPreferences("PrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cookie", token);
        editor.commit();
        SESSION_TOKEN = token; }
    public static String getToken() {return SESSION_TOKEN;}
}
