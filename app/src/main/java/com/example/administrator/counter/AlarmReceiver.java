package com.example.administrator.counter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by YS on 2015-12-12.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("com.example.administrator.ALARM_RING")) {
            // TODO Auto-generated method stub
            //토스트 찍고~
            Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();

            NotificationManager notifier = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //노티피케이션바에 나타낼 아이콘이미지
            Notification notify = new Notification(R.drawable.ic_launcher, "text",
                    System.currentTimeMillis());

            Intent intent2 = new Intent(context, Alarm.class);
            //PendingIntent pender = PendingIntent
            //  .getActivity(context, 0, intent2, 0);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, Alarm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("sendId", intent.getExtras().getString("name")),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notify.setLatestEventInfo(context, "해야할일이 있어요", "banhong test", contentIntent);
            //노티피케이션에서 선택하면 표시를 없앨지 말지 설정
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            //진동 설정
            notify.vibrate = new long[]{200, 200, 500, 300};
            //사운드 설정
            // notify.sound=Uri.parse("file:/");
            notify.number++;

            //노티를 던진다!
            notifier.notify(1, notify);
        } else if (action.equals("com.example.administrator.ALARM_END")) {
            // TODO Auto-generated method stub
            //토스트 찍고~
            Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();

            NotificationManager notifier = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //노티피케이션바에 나타낼 아이콘이미지
            Notification notify = new Notification(R.drawable.ic_launcher, "text",
                    System.currentTimeMillis());

            Intent intent2 = new Intent(context, Alarm.class);
            //PendingIntent pender = PendingIntent
            //  .getActivity(context, 0, intent2, 0);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, Alarm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("sendId", intent.getExtras().getString("name")),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notify.setLatestEventInfo(context, "할일을 못했어요ㅠ", "banhong test", contentIntent);
            //노티피케이션에서 선택하면 표시를 없앨지 말지 설정
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            //진동 설정
            notify.vibrate = new long[]{200, 200, 500, 300};
            //사운드 설정
            // notify.sound=Uri.parse("file:/");
            notify.number++;
        }
    }

}
