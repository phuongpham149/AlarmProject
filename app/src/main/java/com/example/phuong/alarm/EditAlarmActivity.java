package com.example.phuong.alarm;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.phuong.alarm.databases.DatabaseAlarm;
import com.example.phuong.alarm.models.Alarm;

import java.util.Calendar;
/**
 * Created by phuong on 08/12/2016.
 */

public class EditAlarmActivity extends AppCompatActivity implements View.OnClickListener  {
    public static String INTENT_EDIT = "intentEdit";
    private TextView mTvSetTime;
    private TextView mTvRepeatDay;
    private CheckBox mCbActive;
    private String mHourSelect = "";
    private String mMinSelect = "";
    private Calendar mcurrentTime = Calendar.getInstance();
    private String mDayRepeat = "";
    private String mDayRepeatInt = "";
    private Alarm mAlarm;
    private DatabaseAlarm mDatabaseAlarm;
    private RelativeLayout mRelativeLayoutSetTime;
    private RelativeLayout mRelativeLayoutRepeat;
    private int mPosition = 0;
    public static int RESULT_CODE_EDIT = 20000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_alarm);
        mRelativeLayoutSetTime = (RelativeLayout) findViewById(R.id.layoutSetTime);
        mRelativeLayoutRepeat = (RelativeLayout) findViewById(R.id.layoutRepeat);
        mTvSetTime = (TextView) findViewById(R.id.tvSetTime);
        mCbActive = (CheckBox) findViewById(R.id.tvCheck);
        mTvRepeatDay = (TextView) findViewById(R.id.tvRepeatDay);

        mAlarm = getIntent().getParcelableExtra(MainActivity.INTENT_KEY);
        mPosition = getIntent().getIntExtra(MainActivity.INTENT_KEY_POSITION, 0);
        mTvSetTime.setText(mAlarm.getHour() + ":" + mAlarm.getMin());
        mTvRepeatDay.setText(mAlarm.getRepeartChar());
        mCbActive.setChecked(mAlarm.isStatus());
        mDayRepeatInt = mAlarm.getRepeart();
        mDayRepeat = mAlarm.getRepeartChar();

        mDatabaseAlarm = new DatabaseAlarm(this);
        mDatabaseAlarm.open();

        mRelativeLayoutSetTime.setOnClickListener(this);
        mRelativeLayoutRepeat.setOnClickListener(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(R.string.edit_alarm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_alarm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                if (("").equals(mHourSelect))
                    mHourSelect = mAlarm.getHour();
                if (("").equals(mMinSelect))
                    mMinSelect = mAlarm.getMin();

                //get checkbox
                boolean status = false;
                status = mCbActive.isChecked();
                //edit
                mDatabaseAlarm.editData(mAlarm.getId(), mHourSelect, mMinSelect, mDayRepeatInt, mDayRepeat, String.valueOf(status));

                Intent intent = new Intent();
                intent.putExtra(INTENT_EDIT, new Alarm(mHourSelect, mMinSelect, mDayRepeatInt, status, mDayRepeat));
                intent.putExtra(MainActivity.INTENT_KEY_POSITION, mPosition);
                setResult(RESULT_CODE_EDIT, intent);

                broadcastIntent(new Alarm(mHourSelect, mMinSelect, mDayRepeatInt, status, mDayRepeat));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutSetTime:
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTvSetTime.setText(selectedHour + ":" + selectedMinute);
                        mHourSelect = String.valueOf(selectedHour);
                        mMinSelect = String.valueOf(selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
                break;
            case R.id.layoutRepeat:
                showDialogRepeat();
                break;
        }
    }

    public void showDialogRepeat() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Repeat");
        dialog.setContentView(R.layout.dialog_time_repeat);
        final CheckBox mCbMonday = (CheckBox) dialog.findViewById(R.id.cbMonday);
        final CheckBox mCbTuesday = (CheckBox) dialog.findViewById(R.id.cbTuesday);
        final CheckBox mCbWebnesday = (CheckBox) dialog.findViewById(R.id.cbWednesday);
        final CheckBox mCbThursday = (CheckBox) dialog.findViewById(R.id.cbThursday);
        final CheckBox mCbFriday = (CheckBox) dialog.findViewById(R.id.cbFriday);
        final CheckBox mCbSaturday = (CheckBox) dialog.findViewById(R.id.cbSaturday);
        final CheckBox mCbSunday = (CheckBox) dialog.findViewById(R.id.cbSunday);
        Button mBtnOk = (Button) dialog.findViewById(R.id.btnOk);

        if ("Every Day".equals(mDayRepeat)) {
            mCbMonday.setChecked(true);
            mCbTuesday.setChecked(true);
            mCbWebnesday.setChecked(true);
            mCbThursday.setChecked(true);
            mCbFriday.setChecked(true);
            mCbSaturday.setChecked(true);
            mCbSunday.setChecked(true);
        } else {
            String[] days = mDayRepeat.split("[,]");
            for (int i = 0; i < days.length; i++) {
                if (days[i].equals(mCbMonday.getText().toString()))
                    mCbMonday.setChecked(true);
                if (days[i].equals(mCbTuesday.getText().toString()))
                    mCbTuesday.setChecked(true);
                if (days[i].equals(mCbWebnesday.getText().toString()))
                    mCbWebnesday.setChecked(true);
                if (days[i].equals(mCbThursday.getText().toString()))
                    mCbThursday.setChecked(true);
                if (days[i].equals(mCbFriday.getText().toString()))
                    mCbFriday.setChecked(true);
                if (days[i].equals(mCbSaturday.getText().toString()))
                    mCbSaturday.setChecked(true);
                if (days[i].equals(mCbSunday.getText().toString()))
                    mCbSunday.setChecked(true);
            }
        }

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDayRepeat="";
                mDayRepeatInt="";

                if (mCbMonday.isChecked()) {
                    mDayRepeat += "Monday,";
                    mDayRepeatInt += "2,";
                }
                if (mCbTuesday.isChecked()) {
                    mDayRepeat += "Tuesday,";
                    mDayRepeatInt += "3,";
                }
                if (mCbWebnesday.isChecked()) {
                    mDayRepeat += "Wednesday,";
                    mDayRepeatInt += "4,";
                }
                if (mCbThursday.isChecked()) {
                    mDayRepeat += "Thursday,";
                    mDayRepeatInt += "5,";
                }
                if (mCbFriday.isChecked()) {
                    mDayRepeat += "Friday,";
                    mDayRepeatInt += "6,";
                }
                if (mCbSaturday.isChecked()) {
                    mDayRepeat += "Saturday,";
                    mDayRepeatInt += "7,";
                }
                if (mCbSunday.isChecked()) {
                    mDayRepeat += "Sunday,";
                    mDayRepeatInt += "1,";
                }

                mTvRepeatDay.setText(mDayRepeat);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void broadcastIntent(Alarm alarm) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.BROADCAST_KEY, alarm);
        intent.putExtras(bundle);
        intent.putExtra(MainActivity.INTENT_KEY_POSITION, mPosition);
        intent.setAction(getString(R.string.broadcast_edit_intent));
        sendBroadcast(intent);
    }
}
