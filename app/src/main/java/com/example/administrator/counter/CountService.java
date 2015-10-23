package com.example.administrator.counter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



public class CountService extends Service
{
    private int     mCurNum      = 0;
    private Thread  mCountThread = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i("superdroid", "onCreate()");

        // 1. 노티피케이션 객체 생성
        // ====================================================================
        Intent intent = new Intent(this, Count.class );
        PendingIntent pIntent = PendingIntent.getActivity( this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Count Service")
                .setContentText("Running Count Service")
                .setSmallIcon( R.drawable.ic_launcher )
                .setContentIntent( pIntent )
                .build();
        // ====================================================================

        // 2. 포그라운드 서비스 설정 (지각할 수 있는 서비스가 된다.)
        // ====================================================================
        startForeground( 1234, noti );
        // ====================================================================
    }

    @Override
    public int onStartCommand( Intent intent,
                               int flags,
                               int startId )
    {
        super.onStartCommand(intent, flags, startId);

        Log.i("superdroid", "onStartCommand() : " + intent);

        if( mCountThread == null)
        {
            mCountThread = new Thread("Count Thread")
            {
                public void run()
                {
                    while( true )
                    {
                        Log.i("superdroid", "Count : " + mCurNum);

                        mCurNum ++;

                        try{ Thread.sleep( 1000 ); }
                        catch( InterruptedException e )
                        {
                            break;
                        };
                    }
                }
            };

            mCountThread.start();
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy()
    {
        Log.i("superdroid", "onDestroy()");

        stopForeground(true);

        if( mCountThread != null )
        {
            mCountThread.interrupt();
            mCountThread = null;
            mCurNum = 0;
        }

        super.onDestroy();
    }


    public int getCurCountNumber( )
    {
           return mCurNum;
    }

    public class LocalBinder extends Binder{
        CountService getCountService()
        {
            return CountService.this;
        }
    };


    private final Binder mBinder = new LocalBinder();

    public Binder onBind(Intent intent)
    {
        Log.i("superdroid","onBind()");
        return mBinder;
    }

    public boolean onUnbind(Intent intent)
    {
        Log.i("superdroid","onUnbind()");
        return super.onUnbind(intent);
    }

}
