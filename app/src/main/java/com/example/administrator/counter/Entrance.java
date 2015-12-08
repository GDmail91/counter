package com.example.administrator.counter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    ProgressBar pb1;

    ApplicationClass app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);

        Log.d(TAG, "로딩중...");
        app = (ApplicationClass)getApplicationContext();

        pb1 = (ProgressBar)findViewById(R.id.ProgressBar01);
        pb1.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        boolean is_login = prefs.getBoolean("is_login", true);

        Log.d(TAG, "로그인 상태 : " + is_login);

        // 자동 로그인 프로세스
        if (is_login) {
            Log.d(TAG, "자동로그인 대기!");
            user_id.setText(prefs.getString("id", ""));
            user_pw.setText(prefs.getString("password", ""));

            // 로그인 통신 실행
            try {
                String userID = user_id.getText().toString();
                String userPW = user_pw.getText().toString();

                jobj = new JSONObject("{ \"id\" : \"" + userID + "\",\"password\" : \"" + userPW + "\"}");

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

        user_id = (EditText)findViewById(R.id.user_id);
        user_pw = (EditText)findViewById(R.id.user_pw);

        // 로그인 프로세스
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String userID = user_id.getText().toString();
                 String userPW = user_pw.getText().toString();
                 if (userID.equals("") || userPW.equals("")) {
                     Toast toast = Toast.makeText(getApplicationContext(), "비밀번호 또는 아이디를 입력해 주세요.", Toast.LENGTH_LONG);
                     toast.show();
                 } else {
                     // 로그인 통신 실행
                     try {
                         jobj = new JSONObject("{ \"id\" : \"" + userID + "\",\"password\" : \"" + userPW + "\"}");

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
}
