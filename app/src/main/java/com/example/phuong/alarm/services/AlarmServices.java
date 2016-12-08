package com.example.phuong.alarm.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.phuong.alarm.MainActivity;
import com.example.phuong.alarm.R;
import com.example.phuong.alarm.broadcast.AlarmReceiver;
import com.example.phuong.alarm.databases.DatabaseAlarm;
import com.example.phuong.alarm.models.Alarm;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by phuong on 07/12/2016.
 */

public class AlarmServices extends Service {
    private ArrayList<Alarm> mAlarms;
    private DatabaseAlarm mDatabaseAlarm;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.broadcast_delete_intent))) {
                Bundle bundle = intent.getExtras();
                ArrayList<Integer> mPositions = bundle.getIntegerArrayList(MainActivity.BROADCAST_KEY);
                for (int i : mPositions) {
                    mAlarms.remove(i);
                }
                accessAlarm(mAlarms);
            }
            if (intent.getAction().equals(getString(R.string.action_add_repeat))) {
                Bundle bundle = intent.getExtras();
                Alarm alarm = bundle.getParcelable(MainActivity.BROADCAST_KEY);
                mAlarms.add(alarm);
                accessAlarm(mAlarms);
            }
            if (intent.getAction().equals(getString(R.string.broadcast_edit_intent))) {
                Bundle bundle = intent.getExtras();
                Alarm alarm = bundle.getParcelable(MainActivity.BROADCAST_KEY);
                int position = intent.getIntExtra(MainActivity.INTENT_KEY_POSITION, 0);
                mAlarms.set(position, alarm);
                accessAlarm(mAlarms);
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        mAlarms = new ArrayList<>();
        mDatabaseAlarm = new DatabaseAlarm(getApplication());
        mDatabaseAlarm.open();
        registerReceiver(mBroadcastReceiver, new IntentFilter(getString(R.string.broadcast_delete_intent)));
        registerReceiver(mBroadcastReceiver, new IntentFilter(getString(R.string.broadcast_add_intent)));
        registerReceiver(mBroadcastReceiver, new IntentFilter(getString(R.string.broadcast_edit_intent)));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAlarms = mDatabaseAlarm.getData();
        accessAlarm(mAlarms);
        return START_STICKY;

    }

    public void accessAlarm(ArrayList<Alarm> Alarms) {
        //time hien tai
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int minsNow = minute + hour * 60;
        int minsAlarm = 0;
        for (int i = 0 ; i<mAlarms.size();i++) {
            if (("true").equals(String.valueOf(mAlarms.get(i).isStatus()))) {
                String[] days = mAlarms.get(i).getRepeart().split("[,]");
                for (int j = 0; j < days.length; j++) {
                    if (Integer.parseInt(days[j].trim()) == date) {
                        minsAlarm = Integer.parseInt(mAlarms.get(i).getMin()) + 60 * (Integer.parseInt(mAlarms.get(i).getHour()));
                        if (minsAlarm > minsNow) {
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mAlarms.get(i).getHour()));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(mAlarms.get(i).getMin()));
                            Intent myIntent = new Intent(AlarmServices.this, AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(this,i, myIntent, 0);
                            alarmManager = (AlarmManager) this.getSystemService(getBaseContext().ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                    }
                }
            }
        }
    }
}
