package com.example.administrator.counter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by YS on 2015-12-12.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String title = intent.getExtras().getString("title");

        if (action.equals("com.example.administrator.ALARM_RING")) {
            // TODO 여기선 버튼이 처음 울렸을때 동작해야하는거
            //토스트 찍고~
            Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();

            NotificationManager notifier = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //노티피케이션바에 나타낼 아이콘이미지
            Notification notify = new Notification(R.drawable.noti, "text",
                    System.currentTimeMillis());

            Intent intent2 = new Intent(context, Alarm.class);
            //PendingIntent pender = PendingIntent
            //  .getActivity(context, 0, intent2, 0);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, Alarm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("sendId", intent.getExtras().getString("name")),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notify.setLatestEventInfo(context, title + " 해야해요", "banhong test", contentIntent);
            //노티피케이션에서 선택하면 표시를 없앨지 말지 설정
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            //진동 설정
            notify.vibrate = new long[]{200, 200, 500, 300};
            //사운드 설정
            // notify.sound=Uri.parse("file:/");
            notify.number++;

            //노티를 던진다!
            notifier.notify(1, notify);

            ///////////////위에는 복붙
            getMessage(context, title + " 해야해요");
        } else if (action.equals("com.example.administrator.ALARM_END")) {
            // TODO 여기선 버튼이 끝날때 동작해야하는거
            //토스트 찍고~
            Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();

            NotificationManager notifier = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //노티피케이션바에 나타낼 아이콘이미지
            Notification notify = new Notification(R.drawable.noti, "text",
                    System.currentTimeMillis());

            Intent intent2 = new Intent(context, Alarm.class);
            //PendingIntent pender = PendingIntent
            //  .getActivity(context, 0, intent2, 0);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, Alarm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("sendId", intent.getExtras().getString("name")),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notify.setLatestEventInfo(context, title + " 수행시간이 끝났어요", "banhong test", contentIntent);
            //노티피케이션에서 선택하면 표시를 없앨지 말지 설정
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            //진동 설정
            notify.vibrate = new long[]{200, 200, 500, 300};
            //사운드 설정
            // notify.sound=Uri.parse("file:/");
            notify.number++;

            ///////////////위에는 복붙
            getMessage(context, title + " 수행시간이 끝났어요");
        }
    }



    // 팝업 메세지 알림
    private void getMessage(Context context, String data) {
        SharedPreferences prefs = context.getSharedPreferences("PrefName", context.MODE_PRIVATE);
        boolean is_popup = prefs.getBoolean("is_login", true);

        Log.d(TAG, "메세지 팝업 함수");
        // 만약 팝업 알림 설정이 켜져있으면 실행한다.
        if (is_popup) {
            Log.d(TAG, "메세지 팝업 올림");
            // 팝업으로 사용할 액티비티를 호출할 인텐트를 작성한다.
            Intent popupIntent = new Intent(context, ShowMessage.class)
                    .putExtra("message", data)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 그리고 호출한다.
            context.startActivity(popupIntent);
        }
    }

}
