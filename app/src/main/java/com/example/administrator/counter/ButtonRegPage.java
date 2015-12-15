package com.example.administrator.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by YS on 2015-12-15.
 */
public class ButtonRegPage extends Activity {

    private ImageButton mButtonAction1;
    private ImageButton mButtonAction2;
    private ImageButton mButtonAction3;
    private ImageButton mButtonAction4;
    private ImageButton mButtonAction5;
    private ImageButton mButtonAction6;

    private String MAC_ADDR;

    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_reg_page);

        mButtonAction1 = (ImageButton) findViewById(R.id.action1);
        mButtonAction2 = (ImageButton) findViewById(R.id.action2);
        mButtonAction3 = (ImageButton) findViewById(R.id.action3);
        mButtonAction4 = (ImageButton) findViewById(R.id.action4);
        mButtonAction5 = (ImageButton) findViewById(R.id.action5);
        mButtonAction6 = (ImageButton) findViewById(R.id.action6);

        MAC_ADDR = getIntent().getStringExtra("mac_addr");

        // 첫번째 버튼
        mButtonAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ButtonRegPage.this, Count.class);
                intent.putExtra("mac_addr", MAC_ADDR);
                intent.putExtra("flag", "set");
                startActivity(intent);
            }
        });

        // 두번째 버튼
        mButtonAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonRegPage.this, Alarm.class);
                intent.putExtra("mac_addr", MAC_ADDR);
                intent.putExtra("flag", "set");
                startActivity(intent);
            }
        });

        // 세번째 버튼
        mButtonAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ButtonRegPage.this, Check.class);
                intent.putExtra("mac_addr", MAC_ADDR);
                intent.putExtra("flag", "set");
                startActivity(intent);
            }
        });

        // 네번째 버튼
        mButtonAction4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonRegPage.this, DownTimer.class);
                intent.putExtra("mac_addr", MAC_ADDR);
                intent.putExtra("flag", "set");
                startActivity(intent);
            }
        });

        // 다섯번째 버튼
        mButtonAction5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonRegPage.this, Message.class);
                intent.putExtra("mac_addr", MAC_ADDR);
                intent.putExtra("flag", "set");
                startActivity(intent);
            }
        });

    }
}
