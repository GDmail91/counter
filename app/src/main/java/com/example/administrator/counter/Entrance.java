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
        String id = prefs.getString("id", "");
        String password = prefs.getString("password", "");
        boolean is_login = prefs.getBoolean("is_login", true);

        Log.d(TAG, "로그인 상태 : " + is_login);

        // 자동 로그인 프로세스
        if (is_login) {
            Log.d(TAG, "자동로그인 대기!");
            autoLogin(id, password);
        }

        pb1.setVisibility(View.INVISIBLE);

        user_id = (EditText)findViewById(R.id.user_id);
        user_pw = (EditText)findViewById(R.id.user_pw);


        // 로그인 프로세스
        loginProcess();


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
     * 자동 로그인 프로세스
     */
    private void autoLogin(String id, String password) {
        Log.d(TAG, "로그인 통신 실행");

        try {
            user_id.setText(id);
            user_pw.setText(password);

            jobj = new JSONObject("{ id : " + user_id.getText() + ",password : " + user_pw.getText() + "}");

            // 자동로그인 통신 실행
            new HttpClient().setActControl(Entrance.this).sendData("user/login", jobj) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 로그인 프로세스
     */
    public void loginProcess() {

        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "로그인 통신 실행");

                if (user_id.getText().toString().equals("") || user_pw.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "비밀번호 또는 아이디를 입력해 주세요.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    try {
                        jobj = new JSONObject("{ id : " + user_id.getText() + ",password : " + user_pw.getText() + "}");

                        // 통신 실행
                        new HttpClient().setActControl(Entrance.this).sendData("user/login", jobj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
