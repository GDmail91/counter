package com.example.administrator.counter;

import android.util.Log;

/**
 * Created by Administrator on 2015-12-12.
 */
public class stopwatch_r2 {

    private Thread mCountThread = null;
    int mCurNum = 0;
    public void start_stopwatch() {

        if (mCountThread == null)
        {
            mCountThread = new Thread("Count Thread") {
                public void run() {
                    while (true) {
                        Log.i("superdroid", "Count : " + mCurNum);

                        mCurNum++;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        ;
                    }
                }
            };

            mCountThread.start();
        }
    }


    public void stop_stopwatch(){
        mCountThread.stop();
        Log.d("superdroid",String.valueOf(mCurNum));

    }
}

