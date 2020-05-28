package com.example.remed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
    }
}
