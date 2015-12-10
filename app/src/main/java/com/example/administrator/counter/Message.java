package com.example.administrator.counter;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by YS on 2015-12-10.
 */
public class Message extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.message_input);

    }

    // 메세지 등록 버튼 클릭
    public void submit_message() {
        // TODO 메세지 등록된것 서버로 보냄
    }
}
