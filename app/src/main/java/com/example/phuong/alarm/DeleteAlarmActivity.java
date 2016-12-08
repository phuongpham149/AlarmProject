package com.example.phuong.alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.phuong.alarm.adapters.RecyclerViewAlarmDeleteAdapter.RecyclerViewAlarmDeleteAdapter;
import com.example.phuong.alarm.databases.DatabaseAlarm;
import com.example.phuong.alarm.listeners.RecyclerItemListener;
import com.example.phuong.alarm.models.Alarm;

import java.util.ArrayList;

/**
 * Created by phuong on 07/12/2016.
 */

public class DeleteAlarmActivity extends AppCompatActivity implements View.OnClickListener,RecyclerItemListener {
    public static String INTENT_DELETE = "intent_delete";
    public static int RESULT_CODE_DELETE = 20000;
    private RecyclerView mRecyclerView;
    private RecyclerViewAlarmDeleteAdapter mRecyclerViewAlarmAdapter;
    private ArrayList<Alarm> mAlarms;
    private DatabaseAlarm mDatabaseAlarm;
    private ArrayList<Alarm> mAlarmsSelected = new ArrayList<>();
    private Button mBtnDelete;
    private Button mBtnCancel;
    private ArrayList<Integer> mPositions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_alarm);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleViewDelete);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mDatabaseAlarm = new DatabaseAlarm(this);
        mDatabaseAlarm.open();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(R.string.delete_alarm);


        mAlarms = new ArrayList<>();
        mAlarms = initData();
        mRecyclerViewAlarmAdapter = new RecyclerViewAlarmDeleteAdapter(mAlarms, this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAlarmAdapter);

        mBtnDelete.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mPositions.size() > 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList(INTENT_DELETE, mPositions);
                    intent.putExtras(bundle);
                    setResult(RESULT_CODE_DELETE, intent);
                    finish();
                } else
                    onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Alarm> initData() {
        ArrayList<Alarm> alarmArrayList = mDatabaseAlarm.getData();
        return alarmArrayList;
    }

    @Override
    public void onItemclick(int position) {

    }

    @Override
    public void onItemChecked(int position, boolean status) {
        if (status) {
            Alarm alarm = mAlarms.get(position);
            mPositions.add(position);
            mAlarmsSelected.add(alarm);
        } else {
            Alarm mAlarm = mAlarms.get(position);
            mPositions.remove(position);
            mAlarmsSelected.remove(mAlarm);
        }
        mBtnDelete.setText(getString(R.string.delete_alarm_btn_delete) + "(" + mAlarmsSelected.size() + ")");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                showDialogDelete();
                break;
            case R.id.btnCancel:
                if (mPositions.size() > 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList(INTENT_DELETE, mPositions);
                    intent.putExtras(bundle);
                    setResult(RESULT_CODE_DELETE, intent);
                    finish();
                } else
                    onBackPressed();
                break;
        }
    }

    public void showDialogDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Are your sure delete your alarm?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (Alarm alarm : mAlarmsSelected) {
                            mDatabaseAlarm.deleteData(alarm.getId());
                            mAlarms.remove(alarm);
                            if (mPositions.size() > 0)
                                broadcastIntent(mPositions);
                            Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
                        }
                        mRecyclerViewAlarmAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void broadcastIntent(ArrayList<Integer> positions) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(MainActivity.BROADCAST_KEY, positions);
        intent.putExtras(bundle);
        intent.setAction(getString(R.string.broadcast_delete_intent));
        sendBroadcast(intent);
    }
}
