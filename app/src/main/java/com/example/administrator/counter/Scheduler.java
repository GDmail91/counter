package com.example.administrator.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by YS on 2015-12-03.
 * 비동기로 스케쥴러가 생성됨
 */
public class Scheduler {

    private static final String TAG = "Scheduler";

    private JSONObject schedule = new JSONObject();
    private Queue ScheduleQueue = new LinkedList();
    public BroadcastReceiver mSchedulerListener = null;

    public Scheduler() {
        try {
            this.schedule.put("ScheduleNum", 0)
                         .put("State", "Active")
                         .put("Order", 0)
                         .put("Funcs", new JSONArray());
            Log.d(TAG, "생성 결과 : true");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Scheduler(JSONArray funcs) {

        boolean result = createSchedule(funcs);

        Log.d(TAG, "생성 결과 : " + result);
    }

    public BroadcastReceiver runSchedule() {
        Log.d(TAG, "Scheduler 실행");

        int[] cFunc = popSchedule();

        // 동시에 실행하는 기능수 만큼 flag 값 선언
        if (cFunc.length == 1) {
            boolean flag = false;
            // 기능 하나 실행
            runFunc(cFunc[0]);
            setOrder(cFunc[0]);
        } else {
            boolean[] flag = new boolean[cFunc.length];
            for (int i = 0; i < cFunc.length; i++) {
                flag[i] = false;
                // 기능 여러개 실행
                runFunc(cFunc[i]);
                setOrder(cFunc[i]);
            }
        }

        IntentFilter scFilter = new IntentFilter();
        scFilter.addAction("com.example.administrator.counter.schedule");
        scFilter.addDataScheme("sample");

        mSchedulerListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO 기능 온거 확인 해서 기능 완료됬는지 보고
            }
        };

        return mSchedulerListener;
    }

    /**
     * 스케쥴(큐) 생성 (JSON형태, 넣을 기능들)
     */
    private boolean createSchedule(JSONArray funcs) {
        try {
            this.schedule.put("Funcs", funcs);

            // TODO DB에 저장

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 스케쥴(큐) 데이터베이스에서 가져오기
     */
    public void loadSchedule(int scheId) {

        // TODO DB에서 스케쥴 가져와서 적재
    }

    /**
     * 큐에 푸쉬(단독)
     */
    public void pushSchedule(int funcNum) {
        try {
            JSONArray funcs = schedule.getJSONArray("Funcs");
            int funcsLength = funcs.length();

            // 현재 스케쥴에 등록된 기능이 없을경우
            if (funcsLength == 0) {
                funcs.put(new JSONObject().put("Priority", 0)
                                          .put("Func", String.valueOf(funcNum))
                );
            } else { // 기능이 있을경우 마지막 우선순위를 가져와서 다음으로 넣음
                JSONObject func = new JSONObject(String.valueOf(funcs.getInt(funcsLength)));
                int cPriority = func.getInt("Priority");

                funcs.put(new JSONObject().put("Priority", cPriority+1)
                                .put("Func", String.valueOf(funcNum))
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 큐에다 푸쉬
        ScheduleQueue.offer(String.valueOf(funcNum));

        // TODO DB에 저장

    }

    /**
     * 큐에 푸쉬(배열)
     */
    public void pushSchedule(int... funcNum) {
        try {
            JSONArray funcs = schedule.getJSONArray("Funcs");
            int funcsLength = funcs.length();

            // 현재 스케쥴에 등록된 기능이 없을경우
            if (funcsLength == 0) {
                for (int mFuncNum : funcNum) {
                    funcs.put(new JSONObject().put("Priority", 0)
                                    .put("Func", String.valueOf(mFuncNum))
                    );
                }
            } else { // 기능이 있을경우 마지막 우선순위를 가져와서 다음으로 넣음
                JSONObject func = new JSONObject(String.valueOf(funcs.getInt(funcsLength)));
                int cPriority = func.getInt("Priority") + 1;

                for (int mFuncNum : funcNum) {
                    funcs.put(new JSONObject().put("Priority", cPriority)
                                    .put("Func", String.valueOf(mFuncNum))
                    );
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 큐에다 푸쉬
        String pushFunc = "";
        for (int i = 0; i < funcNum.length; i++) {
            if (i < funcNum.length-1) {
                pushFunc += String.valueOf(funcNum[i]) + "|";
            } else { // 마지막 인덱스일 경우
                pushFunc += String.valueOf(funcNum[i]);
            }
        }
        ScheduleQueue.offer(pushFunc);

        // TODO DB에 저장

    }

    /**
     * 큐에서 팝
     * return 꺼내온 ID
     */
    private int[] popSchedule() {

        // 큐에서 팝
        String popResult = ScheduleQueue.poll().toString();
        String[] splitResult = popResult.split("|");
        int[] funcId = new int[splitResult.length];
        for (int i = 0; i < splitResult.length; i++) {
            funcId[i] = Integer.valueOf(splitResult[i]);
        }

        return funcId;
    }

    /**
     * 기능 확인
     * return 어떤 동작인지
     */
    public int checkFunction(int funcId) {
        // TODO DB 가서 기능 ID의 동작이 뭔지 확인
        return 0;
    }

    /**
     * 스케쥴(순서) 변경
     * return 성공여부
     */
    public boolean setSchedule(JSONObject Funcs) {

        // TODO 스케쥴의 Funcs 가져와서 바꿔주고 DB에 저장

        return true;
    }

    /**
     * 스케쥴 상태 변경
     * return 성공여부
     */
    public boolean setState(String state) {

        // TODO 스케쥴의 State 가져와서 바꿔주고 DB에 저장

        return true;
    }

    /**
     * 실행될 기능 변경 (사실 이게 실행되는 일이 없어야 함)
     * return 변경된 값
     */
    public int setOrder(int funcId) {
        int priority = 0;

        try {
            JSONArray funcs = schedule.getJSONArray("funcs");

            int cPriority = 0;
            JSONObject func = null;
            // 스케쥴에 등록된 기능 탐색
            for (int i = 0; i < funcs.length(); i++) {
                func = funcs.getJSONObject(i);
                cPriority = func.getInt("Priority");
                // Priority 비교
                if(cPriority == funcId) {
                    priority = cPriority;
                    break;
                }
            }

            // 스케쥴의 Order 변경
            schedule.put("Order", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priority;
    }

    /**
     * 큐(스케쥴) 초기화
     */

    /**
     * 기능 실행
     */
    private void runFunc(int funcId) {
        try {
            // TODO DB의 기능 테이블에서 funcId에 해당하는 정보 가져옴
            //JSONObject func = DB에서 가져오는 함수
            JSONObject func = new JSONObject();
            switch (func.getString("func")) {
                case "Alram":
                    // TODO
                    // 각 기능에선 sendBroadcase() 사용
                    break;

                case "Counter":
                    // TODO
                    // 각 기능에선 sendBroadcase() 사용
                    break;

                case "Checker":
                    // TODO
                    // 각 기능에선 sendBroadcase() 사용
                    break;

                case "Timer":
                    // TODO
                    // 각 기능에선 sendBroadcase() 사용
                    break;

                case "Stopwatch":
                    // TODO
                    // 각 기능에선 sendBroadcase() 사용
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
