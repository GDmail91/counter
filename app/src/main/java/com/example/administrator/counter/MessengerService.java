package com.example.administrator.counter;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 2015-12-01.
 */
public class MessengerService extends Service {

    // 요청 메시지 코드 정의
    // ========================================================================
    static final int REQUEST_MSG_PLUS_2_VALUE     = 1001;
    static final int REQUEST_MSG_MINUS_2_VALUE    = 1002;
    // ========================================================================

    // 응답 메시지 코드 정의
    // ========================================================================
    static final int RESPONSE_MSG_PLUS_2_VALUE    = 2001;
    static final int RESPONSE_MSG_MINUS_2_VALUE   = 2002;
    // ========================================================================

    MsgRequestHanler mMsgRequestHandler = null;
    Messenger        mMessengerService  = null;

    class MsgRequestHanler extends Handler
    {
        @Override
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
                // 두 수의 합을 계산하고 결과를 클라이언트에게 전달한다.
                // ============================================================
                case REQUEST_MSG_PLUS_2_VALUE :
                {
                    int responsePlusValue = msg.arg1 + msg.arg2;

                    Log.i("superdroid", "plus result : " + responsePlusValue );

                    Message replyMsg = Message.obtain( null,
                            RESPONSE_MSG_PLUS_2_VALUE,
                            responsePlusValue, 0 );

                    try
                    {
                        msg.replyTo.send( replyMsg );
                    }
                    catch ( RemoteException e )
                    {
                        e.printStackTrace();
                    }

                    break;
                }
                // ============================================================

                // 두 수의 차을 계산하고 결과를 클라이언트에게 전달한다.
                // ============================================================
                case REQUEST_MSG_MINUS_2_VALUE :
                {
                    int responseMinusValue = msg.arg1 - msg.arg2;

                    Log.i("superdroid", "Minus result : " + responseMinusValue );

                    Message replyMsg = Message.obtain( null,
                            RESPONSE_MSG_MINUS_2_VALUE,
                            responseMinusValue, 0 );

                    try
                    {
                        msg.replyTo.send( replyMsg );
                    }
                    catch ( RemoteException e )
                    {
                        e.printStackTrace();
                    }
                    break;
                }
                // ============================================================
            }
        }
    }


    @Override
    public void onCreate()
    {
        // 요청 메시지를 처리할 핸들러 객체를 생성한다.
        mMsgRequestHandler = new MsgRequestHanler();

        // 메신저 객체를 갱성하고, 메신저가 메시지를 수신받았을 때 메시지 큐에 추가하여
        // 요청을 처리하도록 핸들러를 객체를 생성자로 전달한다.
        mMessengerService  = new Messenger( mMsgRequestHandler );

        super.onCreate();
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        // 클라이언트와 연결되면 통신을 위한 바인더 객체를 클라이언트에게 전달한다.
        return mMessengerService.getBinder();
    }

}
