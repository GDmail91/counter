package com.example.administrator.counter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 영수 on 2015-09-04.
 */
public class ButtonList extends ActionBarActivity {

    private static final String TAG = "ButtonList Activity";


    private ListView m_ListView;    // 버튼 리스트 띄우기 위한 뷰
    private ButtonAdapter m_ListAdapter;    // 버튼 리스트 가져올 어댑터



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         *  로그인 상태 확인
         */
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        boolean is_login = prefs.getBoolean("is_login", false);

        Log.d(TAG, "로그인 상태 : " + is_login);

        // 자동 로그인 프로세스
        if (is_login) {
            Log.d(TAG, "자동로그인 대기!");
            Log.d(TAG, "등록된 ID: " + prefs.getString("id", ""));
            Log.d(TAG, "등록된 PW: " + prefs.getString("password", ""));
            String token = prefs.getString("gcm_token", "");

            ApplicationClass.app = (ApplicationClass)getApplicationContext();
            // 로그인 통신 실행
            try {
                String userID = prefs.getString("id", "");
                String userPW = prefs.getString("password", "");

                JSONObject jobj = new JSONObject()
                        .put("id", userID)
                        .put("password", userPW)
                        .put("reg_id", token);

                // 통신 실행
                new HttpHandler().loginUser(jobj.toString(), new MyCallback() {
                    @Override
                    public void httpProcessing(JSONObject result) {
                        try {
                            // 결과에 따라서 인텐트 생성, 액티비티실행
                            Toast toast;
                            if (result.getBoolean("status")) {

                                toast = Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG);
                                toast.show();

                                Log.d(TAG, "로그인 성공");

                            } else {
                                toast = Toast.makeText(getApplicationContext(), "로그인 실패 : " + result.getString("message"), Toast.LENGTH_LONG);
                                toast.show();

                                Log.d(TAG, "로그인 실패");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.button_list);

        // 클릭키를 추가할 버튼정의
        ImageButton addBtn = (ImageButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "클릭키 추가버튼");
                Intent intent = new Intent(ButtonList.this, NfcReader.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        getButtonTitle();
    }

    // 버튼 목록 얻어오는 함수
    private void getButtonTitle() {

        new HttpHandler().getBtnList(new MyCallback() {
            @Override
            public void httpProcessing(JSONObject result) {
                // TODO 결과에서 타이틀 얻어와야함
                Log.d(TAG, "버튼 목록 콜백 받음");
                Log.d(TAG, result.toString());
                try {
                    Boolean status = result.getBoolean("status");
                    String message = result.getString("message");
                    // 결과에 따라서 인텐트 생성, 액티비티실행
                    if (status) {
                        Log.d(TAG, "버튼정보 가져옴");

                        Log.d(TAG, "버튼 data 값: " + result.getString("data"));

                        JSONArray data = new JSONArray(result.getString("data"));
                        Log.d(TAG, data.toString());

                        ArrayList<String> tmp_List = new ArrayList<String>();
                        ArrayList<String> tmp_Mac = new ArrayList<String>();
                        ArrayList<Integer> tmp_Fid = new ArrayList<Integer>();

                        for (int i = 0; i < data.length(); i++) {
                            // List 어댑터에 전달할 값들
                            tmp_List.add(data.getJSONObject(i).getString("title"));
                            tmp_Mac.add(data.getJSONObject(i).getString("mac_addr"));
                            tmp_Fid.add(data.getJSONObject(i).getInt("fid"));


                            /*m_ListAdapter.add(
                                    data.getJSONObject(i).getString("title"),
                                    data.getJSONObject(i).getString("mac_addr"),
                                    data.getJSONObject(i).getInt("fid")
                            );*/
                        }

                        // ListView 생성하면서 작성할 값 초기화
                        m_ListAdapter = new ButtonAdapter(tmp_List, tmp_Mac, tmp_Fid);

                    } else {
                        Log.d(TAG, "버튼정보 가져오기 실패: " + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // ListView 어댑터 연결
                m_ListView = (ListView) findViewById(R.id.list_view);
                m_ListView.setAdapter(m_ListAdapter);
                m_ListView.setOnItemClickListener(onClickListItem);
            }
        });

    }

    // 아이템 터치 이벤트
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 이벤트 발생 시 해당 아이템 위치의 텍스트를 출력
            Toast.makeText(getApplicationContext(), m_ListAdapter.getItem(position) + "\n 여기서 버튼 상세화면으로 넘어갑니다", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // 로그아웃 프로세스
        if (id == R.id.action_settings) {
            // 로그인 데이터 삭제
            SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.remove("id").remove("password");
            editor.putBoolean("is_login", false);
            editor.commit();

            // 로그인 창으로 이동
            Intent intent = new Intent(ButtonList.this, Entrance.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.test_reg) {
            new HttpHandler().regBtn("{\"mac_addr\":\"TESTMACADDR5\"}", new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    Log.d(TAG, "버튼등록 완료");
                    Toast toast = Toast.makeText(getApplicationContext(), "버튼등록 완료", Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            return true;
        } else if (id == R.id.test_send) {
            new HttpHandler().testSend( new MyCallback() {
                @Override
                public void httpProcessing(JSONObject result) {
                    Log.d(TAG, "버튼등록 완료");
                    Toast toast = Toast.makeText(getApplicationContext(), "버튼등록 완료", Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
