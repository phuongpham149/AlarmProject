package com.example.phuong.alarm.broadcast;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.phuong.alarm.CaculatorActivity;
import com.example.phuong.alarm.R;

/**
 * Created by phuong on 07/12/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i=new Intent(context.getApplicationContext(),CaculatorActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }
}
