package com.example.phuong.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.phuong.alarm.adapters.RecyclerViewAlarmAdapter;
import com.example.phuong.alarm.broadcast.AlarmReceiver;
import com.example.phuong.alarm.databases.DatabaseAlarm;
import com.example.phuong.alarm.listeners.RecyclerItemListener;
import com.example.phuong.alarm.models.Alarm;
import com.example.phuong.alarm.services.AlarmServices;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements RecyclerItemListener {
    private TextView mTvAdd;
    private RecyclerView mRecyclerView;
    private ArrayList<Alarm> mAlarms;
    private RecyclerViewAlarmAdapter mRecyclerViewAlarmAdapter;
    private int REQUEST_CODE_ADD = 1000;
    private int REQUEST_CODE_EDIT = 3000;
    private int REQUEST_CODE_DELETE = 2000;
    private DatabaseAlarm mDatabaseAlarm;
    public static String BROADCAST_KEY = "alarm_boadcast";
    public static String INTENT_KEY_POSITION = "alarm_intent_position";
    public static String INTENT_KEY = "alarm_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvAdd = (TextView) findViewById(R.id.tvAdd);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);

        mAlarms = new ArrayList<>();
        mDatabaseAlarm = new DatabaseAlarm(this);
        mDatabaseAlarm.open();
        mAlarms = initData();
        mDatabaseAlarm.close();

        mRecyclerViewAlarmAdapter = new RecyclerViewAlarmAdapter(mAlarms, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerViewAlarmAdapter);

        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddAlarmActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD);
            }
        });

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_alarm);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(R.string.app_alarm);

        Intent intent = new Intent(this, AlarmServices.class);
        startService(intent);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD && resultCode==(AddAlarmActivity.RESULT_CODE_ADD)){
            Alarm alarm = data.getParcelableExtra(AddAlarmActivity.INTENT_ADD);
            mAlarms.add(alarm);
            mRecyclerViewAlarmAdapter.notifyDataSetChanged();
        }
        if(requestCode==REQUEST_CODE_DELETE && resultCode==(DeleteAlarmActivity.RESULT_CODE_DELETE)){
            Bundle bundle = data.getExtras();
            ArrayList<Integer> mPositionDeleted = bundle.getIntegerArrayList(DeleteAlarmActivity.INTENT_DELETE);
            for (int in : mPositionDeleted) {
                mAlarms.remove(in);
            }
            mRecyclerViewAlarmAdapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_CODE_EDIT && resultCode == (EditAlarmActivity.RESULT_CODE_EDIT)) {
            Alarm alarm = data.getParcelableExtra(EditAlarmActivity.INTENT_EDIT);
            int position = data.getIntExtra(MainActivity.INTENT_KEY_POSITION, 0);
            mAlarms.set(position, alarm);
            mRecyclerViewAlarmAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<Alarm> initData() {
        ArrayList<Alarm> alarms = new ArrayList<>();
        alarms = mDatabaseAlarm.getData();
        return alarms;
    }

    @Override
    public void onItemclick(int position) {
        Alarm alarm = mAlarms.get(position);
        Intent intent = new Intent(MainActivity.this, EditAlarmActivity.class);
        intent.putExtra(INTENT_KEY, alarm);
        intent.putExtra(INTENT_KEY_POSITION, position);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    public void onItemChecked(int position, boolean status) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Intent intent = new Intent(MainActivity.this, DeleteAlarmActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DELETE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
