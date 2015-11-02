package com.example.administrator.counter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.counter.CountService.LocalBinder;
/**
 * Created by Administrator on 2015-09-18.
 */
public class Count extends Activity {
    private static final String TAG = "super_CountActivity";
    CountService mCountService = null;

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected( ComponentName name,
                                        IBinder service )
        {
            Log.d( "superdroid", "onServiceConnected()" );
            mCountService = ((LocalBinder)service).getCountService();
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {

            Log.d( "superdroid", "onServiceDisconnected()" );
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.count);

        Log.d(TAG, "여기 1차");
        // 카운트 서비스 연결
        // ====================================================================
        // Intent serviceIntent = new Intent( this, CountService.class );
        Intent serviceIntent = new Intent( "com.example.administrator.counter.CountService" );
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        // ====================================================================
    }

    public void onClick( View v )
    {
        switch( v.getId() )
        {
            // 1. 카운트 시작
            // ================================================================
            case R.id.start_count_btn:
            {
                Intent serviceIntent =
                        new Intent( this, CountService.class );

                startService( serviceIntent );

                break;
            }
            // ================================================================

            // 2. 카운트 종료
            // ================================================================
            case R.id.stop_count_btn:
            {
                Intent serviceIntent =
                        new Intent( this, CountService.class );

                stopService(serviceIntent);
                break;
            }
            // ================================================================

            // 3. 현재까지 카운트 된 수치 보기
            // ================================================================
            case R.id.get_cur_number_btn:
            {

                Toast.makeText( Count.this,
                        "Cur Count : " +
                                mCountService.getCurCountNumber(),
                        Toast.LENGTH_LONG ).show();
                break;
            }
            // ================================================================

            // 4. 바인드 삭제
            // ================================================================
            case R.id.delete_count_btn:
            {

                // 카운트 서비스 해제
                // ====================================================================
                unbindService(mConnection);
                // ====================================================================

                break;
            }
            // ================================================================
        }
    }
}
