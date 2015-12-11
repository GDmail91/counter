package com.example.administrator.counter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 영수 on 2015-10-17.
 *
 * 처음 실행 액티비티 (로딩 프로세스)
 * if (Not Login) {
 *     Login processing
 * } else {
 *     Load ButtonList Activity
 * }
 */
public class Entrance extends Activity {

    private static final String TAG = "Entrance";

    // 로그인 관련 변수
    private EditText user_id;
    private EditText user_pw;
    private JSONObject jobj;

    // GCM 관련 변수
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ProgressBar pb1;

    ApplicationClass app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);

        // GCM 토큰생성 받는 리시버
        registBroadcastReceiver();

        Log.d(TAG, "로딩중...");
        app = (ApplicationClass)getApplicationContext();

        pb1 = (ProgressBar)findViewById(R.id.ProgressBar01);
        pb1.setVisibility(View.VISIBLE);
        user_id = (EditText)findViewById(R.id.user_id);
        user_pw = (EditText)findViewById(R.id.user_pw);

        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        boolean is_login = prefs.getBoolean("is_login", true);

        Log.d(TAG, "로그인 상태 : " + is_login);

        // 자동 로그인 프로세스
        if (is_login) {
            Log.d(TAG, "자동로그인 대기!");
            Log.d(TAG, "등록된 ID: " + prefs.getString("id", ""));
            Log.d(TAG, "등록된 PW: " + prefs.getString("password", ""));
            user_id.setText(prefs.getString("id", ""));
            user_pw.setText(prefs.getString("password", ""));
            String token = prefs.getString("gcm_token", "");

            ApplicationClass.app = (ApplicationClass)getApplicationContext();
            // 로그인 통신 실행
            try {
                String userID = user_id.getText().toString();
                String userPW = user_pw.getText().toString();

                jobj = new JSONObject()
                            .put("id", userID)
                            .put("password", userPW)
                            .put("reg_id", token);

                // 통신 실행
                new HttpHandler().loginUser(jobj.toString(), new MyCallback() {
                    @Override
                    public void httpProcessing(JSONObject result) {
                        loginProcess(result);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pb1.setVisibility(View.INVISIBLE);

        // 로그인 프로세스
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Log.d(TAG, "로그인 프로세스 시작");

                 SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                 if (prefs.getString("gcm_token", "").isEmpty()) {
                     Log.d(TAG, "GCM 토큰 생성");
                     // GCM 토큰 생성
                     getInstanceIdToken();
                 } else {
                     Log.d(TAG, "GCM 토큰 생성안함");
                     login();
                 }
             }
         });

        /**
         * 회원가입 버튼
         */
        Button regi_btn = (Button)findViewById(R.id.regi_btn);
        regi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Entrance.this, UserRegistration.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 로그인 프로세스 시작
     */
    public void login() {
        Log.d(TAG, "login()함수 실행");
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        String token = prefs.getString("gcm_token", "");
        String userID = user_id.getText().toString();
        String userPW = user_pw.getText().toString();

        if (userID.equals("") || userPW.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "비밀번호 또는 아이디를 입력해 주세요.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            ApplicationClass.app = (ApplicationClass)getApplicationContext();

            // 로그인 통신 실행
            try {
                jobj = new JSONObject()
                        .put("id", userID)
                        .put("password", userPW)
                        .put("reg_id", token);

                // 통신 실행
                new HttpHandler().loginUser(jobj.toString(), new MyCallback() {
                    @Override
                    public void httpProcessing(JSONObject result) {
                        loginProcess(result);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 로그인 프로세스
     */
    public void loginProcess(JSONObject result) {

        Log.d(TAG, "로그인 콜백 받음");
        try {

            Toast toast;
            // 결과에 따라서 인텐트 생성, 액티비티실행
            if (result.getBoolean("status")) {
                // Preferences에 id, password 저장
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.remove("id").remove("password");
                editor.putString("id", user_id.getText().toString());
                editor.putString("password", user_pw.getText().toString());
                editor.putBoolean("is_login", true);
                editor.commit();

                toast = Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG);
                toast.show();

                Log.d(TAG, "로그인 성공");

                Log.d(TAG, "GCM 토큰 : " + prefs.getString("gcm_token", ""));

                Intent intent = new Intent(Entrance.this, ButtonList.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                toast = Toast.makeText(getApplicationContext(), "로그인 실패 : " + result.getString("message"), Toast.LENGTH_LONG);
                toast.show();

                Log.d(TAG, "로그인 실패");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // GCM 토큰 생성
    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            Log.d(TAG, "토큰 생성 서비스 실행");

        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    // TODO
                    Log.d(TAG, "READY");
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    // TODO
                    Log.d(TAG, "GENERATING");
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    Log.d(TAG, "COMPLETE");

                    SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String token = intent.getStringExtra("token");
                    editor.putString(token, "");
                    Log.d(TAG, "토큰생성완료: "+token);

                    login();

                }

            }
        };
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
