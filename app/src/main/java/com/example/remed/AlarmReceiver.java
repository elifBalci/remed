package com.example.remed;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    static String alarmText;

    public static void setAlarmText(String readedAlarmText) {
        alarmText = readedAlarmText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, alarmText, Toast.LENGTH_LONG).show();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        final int interval = 8000; // 8 Second
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                ringtone.stop();
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);

    }
}
