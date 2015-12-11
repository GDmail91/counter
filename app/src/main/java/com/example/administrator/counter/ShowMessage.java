package com.example.administrator.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by YS on 2015-12-10.
 */
public class ShowMessage extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
        setContentView(R.layout.show_message);
;
        // 이 부분이 바로 화면을 깨우는 부분 되시겠다.
        // 화면이 잠겨있을 때 보여주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        TextView tv = (TextView)findViewById(R.id.message);
        tv.setText(getIntent().getStringExtra("message"));
        Button ib = (Button)findViewById(R.id.confirm);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 확인버튼을 누르면 앱의 런처액티비티를 호출한다.
                Intent intent = new Intent(ShowMessage.this, ButtonList.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

}
