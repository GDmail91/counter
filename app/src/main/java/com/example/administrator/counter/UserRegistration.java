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
 */
public class UserRegistration extends Activity {

    private static final String TAG = "UserRegistration";

    // 로그인 관련 변수
    private EditText user_id;
    private EditText user_pw;
    private JSONObject jobj = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist_layout);

        user_id = (EditText) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);

        Button loginProcess = (Button) findViewById(R.id.regi_btn);
        loginProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 회원가입할때 토큰 생성
                    registration();
                    Log.d(TAG, "토큰생성");

                    // id와 password는 통신전에 셋팅 (reg_id는 토큰생성후)
                    jobj.put("id", user_id.getText());
                    jobj.put("password", user_pw.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 회원가입 프로세스
     */
    public void registration() {
        // 여기서 통신 실행

        ProgressBar pb1 = (ProgressBar) findViewById(R.id.ProgressBar01);
        pb1.setVisibility(View.VISIBLE);

        Log.d(TAG, "회원가입 통신 실행");

        // 회원가입 통신 실행
        new HttpHandler().joinUser(jobj.toString(), new MyCallback() {
            @Override
            public void httpProcessing(JSONObject result) {
                Log.d(TAG, "회원가입 콜백 받음");
                try {
                    /**
                     *  id, password 저장, 프로그레스바 제거 및 액티비티 변경
                     */

                    // Preferences에 id, password 저장
                    SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    // 프로그래스바 제거
                    ProgressBar pb1 = (ProgressBar) findViewById(R.id.ProgressBar01);
                    pb1.setVisibility(View.INVISIBLE);

                    Toast toast;
                    // 결과에 따라서 인텐트 생성, 액티비티실행
                    if (result.getBoolean("status")) {
                        editor.putString("id", user_id.getText().toString());
                        editor.putString("password", user_pw.getText().toString());
                        editor.putBoolean("is_login", true);
                        editor.commit();

                        toast = Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG);
                        toast.show();

                        Log.d(TAG, "회원가입 성공");

                        Intent nextIntent = new Intent(UserRegistration.this, Entrance.class);
                        nextIntent.addFlags(nextIntent.FLAG_ACTIVITY_CLEAR_TASK);
                        nextIntent.addFlags(nextIntent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nextIntent);

                    } else {
                        toast = Toast.makeText(getApplicationContext(), "회원가입 실패 : " + result.getString("message"), Toast.LENGTH_LONG);
                        toast.show();

                        Log.d(TAG, "회원가입 실패 : " + result.getString("message"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
