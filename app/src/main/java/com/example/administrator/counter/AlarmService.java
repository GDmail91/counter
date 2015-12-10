package com.example.administrator.counter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Administrator on 2015-12-05.
 */
public class AlarmService extends Service {
    private Thread  mAlarmThread = null;
    private Thread test_Thread = null;
    private int mHour = 0;
    private int mMinute = 0;
    private int h1;
    private int m1;
    private int h2;
    private int m2;
    private int hour1;
    private int minute1;
    private int hour2;
    private int minute2;

    public void getInfo(int ho1,int mi1,int ho2,int mi2){

        hour1 = ho1;
        minute1 = mi1;
        hour2 = ho2;
        minute2 = mi2;
        Log.d("super", "here!!");
        if( test_Thread == null) {
            test_Thread = new Thread("Alarm test_Thread") {
                public void run() {
                    while (true) {
                        Log.i("superdroid", "hour : " + mHour);
                        Log.i("superdroid", "minute : " + mMinute);
                        final Calendar c = Calendar.getInstance();

                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);
                        Log.i("superdroid", "현재시 : " + mHour);
                        Log.i("superdroid", "현재분 : " + mMinute);
                        Log.i("superdroid", "설정시간1 : " + hour1);
                        Log.i("superdroid", "설정시간2 : " + hour2);
                        int original_hour = hour2;
                        if (hour1>12 &&hour2 < 12) {
                            hour2 = hour2 + 24;
                        }
                        if (mHour < 12) {
                            mHour = mHour + 24;
                            Log.i("superdroid",toString().valueOf(mHour));

                            if (mHour <= hour2 && mHour >= hour1) {
                                Log.i("superdroid", "GOOD ALARAM11!!");
                            }
                        };
                        if (mHour > 12) {
                            Log.i("superdroid", "설정1 : " + hour1);
                            Log.i("superdroid", "설정2 : " + hour2);
                            if (mHour <= hour2 && mHour >= hour1) {
                                Log.i("superdroid", "GOOD ALARAM22!!");
                                break;
                            };
                        }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                break;
                            }
                            ;
                        }

                }
            };
                test_Thread.start();
            }
        }
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
                .setContentTitle("Alarm Service")
                .setContentText("Running Alarm Service")
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
        Log.d("super","ok!!!");
        h1 =intent.getIntExtra("h1",1);
        m1 =intent.getIntExtra("m1",2);
        h2 =intent.getIntExtra("h2",3);
        m2 =intent.getIntExtra("m2",4);
        hour1 =intent.getIntExtra("hour1",1);
        minute1 =intent.getIntExtra("minute1",2);
        hour2 =intent.getIntExtra("hour2",3);
        minute2 =intent.getIntExtra("minute2",4);
        super.onStartCommand(intent, flags, startId);
        Log.d("test", String.valueOf(h1));
        Log.d("test",String.valueOf(m1));
        Log.d("test",String.valueOf(h2));
        Log.d("test",String.valueOf(m2));
        Log.i("superdroid", "onStartCommand() : " + intent);

        if( mAlarmThread == null)
        {
            mAlarmThread = new Thread("Alarm Thread")
            {
                public void run()
                {
                    while( true )
                    {
                        Log.i("superdroid", "hour : " + mHour);
                        Log.i("superdroid", "minute : " +mMinute);
                        final Calendar c = Calendar.getInstance();

                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);



                        try{ Thread.sleep( 1000 ); }
                        catch( InterruptedException e )
                        {
                            break;
                        };
                    }
                }
            };

            mAlarmThread.start();
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy()
    {
        Log.i("superdroid", "onDestroy()");

        stopForeground(true);

        if( mAlarmThread != null )
        {
            mAlarmThread.interrupt();
            mAlarmThread = null;
            mHour = 0;
            mMinute = 0;
        }

        super.onDestroy();
    }


    public int getCurCountNumber( )
    {
        if(mMinute == m1) {
            return mMinute;
        }
        else return 0;
    }

    public class LocalBinder extends Binder {
        AlarmService getAlarmService()
        {
            return AlarmService.this;
        }
    };


    private final Binder mBinder = new LocalBinder();

    public Binder onBind(Intent intent)
    {
        Log.i("superdroid", "onBind()");
        return mBinder;
    }

    public boolean onUnbind(Intent intent)
    {
        Log.i("superdroid","onUnbind()");
        return super.onUnbind(intent);
    }

}
