package com.example.administrator.counter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2015-12-02.
 */
public class Messenger extends Activity {


    // 본 코드 값은 서비스 측과 동일하게 설정되어야 한다.
    // ========================================================================
    // 요청 메시지 코드 정의
    static final int REQUEST_MSG_PLUS_2_VALUE     = 1001;
    static final int REQUEST_MSG_MINUS_2_VALUE    = 1002;
    // 응답 메시지 코드 정의
    static final int RESPONSE_MSG_PLUS_2_VALUE    = 2001;
    static final int RESPONSE_MSG_MINUS_2_VALUE   = 2002;
    // ========================================================================

    private android.os.Messenger mMessengerService;

    ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mMessengerService = new android.os.Messenger( service );
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mMessengerService = null;
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.service );

        Intent serviceIntent =
                new Intent("com.superdroid.service.messenger");
        bindService( serviceIntent, mConnection, BIND_AUTO_CREATE );
    }

    @Override
    protected void onDestroy()
    {
        unbindService( mConnection );

        super.onDestroy();
    }

    Handler mMessengerResponseHandler = new Handler()
    {
        @Override
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
                // 두 수의 합의 결과 처리
                // ============================================================
                case RESPONSE_MSG_PLUS_2_VALUE :
                {
                    Toast.makeText( Messenger.this,
                            "20 + 10 = " + msg.arg1,
                            Toast.LENGTH_LONG ).show();
                    break;
                }
                // ============================================================

                // 두 수의 차의 결과 처리
                // ============================================================
                case RESPONSE_MSG_MINUS_2_VALUE :
                {
                    Toast.makeText( Messenger.this,
                            "20 - 10 = " + msg.arg1,
                            Toast.LENGTH_LONG ).show();
                    break;
                }
                // ============================================================
            }
        }
    };

    // 클라이언트의 메신저 객체, 서비스에게 전달하여 응답 결과를 받기위한 메신저이다.
    private final android.os.Messenger mMessengerResponse =
            new android.os.Messenger( mMessengerResponseHandler );


    public void onClick( View v )
    {
        switch ( v.getId() )
        {
            case R.id.plus_btn :
            {
                try
                {
                    Message msg = Message.obtain( null,
                            REQUEST_MSG_PLUS_2_VALUE,
                            20,
                            10);

                    msg.replyTo = mMessengerResponse;
                    mMessengerService.send( msg );
                }
                catch( RemoteException e )
                {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.minus_btn :
            {
                try
                {
                    Message msg = Message.obtain( null,
                            REQUEST_MSG_MINUS_2_VALUE,
                            20,
                            10);

                    msg.replyTo = mMessengerResponse;
                    mMessengerService.send( msg );
                }
                catch( RemoteException e )
                {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
