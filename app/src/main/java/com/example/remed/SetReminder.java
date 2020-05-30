package com.example.remed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetReminder extends AppCompatActivity {
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_reminder_page);

        Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText et = (EditText) findViewById(R.id.editText4);
                    String medName = et.getText().toString().toUpperCase();

                    EditText et2 = (EditText) findViewById(R.id.editText7);
                    String notes = et2.getText().toString();
                    AlarmReceiver.setAlarmText(medName +   ": "  + notes);
                    startAlarmManager();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SetReminder.this);
                    builder.setMessage("Reminder setted for: " + medName.toUpperCase());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.setNegativeButton("CANCEL THE ALARM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopAlarmManager();
                        }
                    });
                    builder.show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startAlarmManager() throws ParseException {
        Intent dialogIntent = new Intent(getBaseContext(), AlarmReceiver.class);

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, dialogIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        EditText et5 = (EditText) findViewById(R.id.editText5);
        String startHourS = et5.getText().toString();
        int startHour= Integer.parseInt(startHourS);

        EditText etMin = (EditText) findViewById(R.id.editTextMin);
        String startMinS = etMin.getText().toString();
        int startMin= Integer.parseInt(startMinS);

        EditText et11 = (EditText) findViewById(R.id.editText11);
        String repeatMS = et11.getText().toString();
        int repeatTime= Integer.parseInt(repeatMS);

        EditText et10 = (EditText) findViewById(R.id.editText10);
        String daysS = et10.getText().toString();
        int days= Integer.parseInt(daysS);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMin);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * repeatTime * 60, pendingIntent);
    }

    public void stopAlarmManager() {
        if(alarmMgr != null)
            alarmMgr.cancel(pendingIntent);
    }

}
