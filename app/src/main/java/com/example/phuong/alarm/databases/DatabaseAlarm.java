package com.example.phuong.alarm.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.phuong.alarm.models.Alarm;

import java.util.ArrayList;

/**
 * Created by phuong on 07/12/2016.
 */

public class DatabaseAlarm {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MIN = "mins";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_DAYS_CHAR = "daysChar";
    public static final String COLUMN_STATUS = "status";
    private static final String DATABASE_NAME = "DB_ALARM";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ALARM";
    private static Context context;
    ArrayList<Alarm> arrInfo = null;
    Alarm alarm = null;
    private SQLiteDatabase db;
    private OpenHelper openHelper;

    public DatabaseAlarm(Context c) {
        context = c;
    }

    public DatabaseAlarm open() throws SQLException {
        openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        openHelper.close();
    }

    public void createData(String hour, String min, String days, String daysChar, String status) {
        String query = "Insert into " + TABLE_NAME + " (" + COLUMN_HOUR + ", " + COLUMN_MIN + ", " + COLUMN_DAYS + ", " + COLUMN_DAYS_CHAR + " , " + COLUMN_STATUS + ") values('" + hour + "','" + min + "',' " + days + "','" + daysChar + "','" + status + "')";
        db.execSQL(query);
    }

    public void editData(int id, String hour, String min, String days, String daysChar, String status) {
        String query = "Update " + TABLE_NAME + " SET " + COLUMN_HOUR + " = '" + hour + "' , " + COLUMN_MIN + " = '" + min + "'," + COLUMN_DAYS + "= '" + days + "'," + COLUMN_DAYS_CHAR + "='" + daysChar + "'," + COLUMN_STATUS + " = '" + status + "' where " + COLUMN_ID + "=" + id;
        Log.d("queryEdit", query);
        db.execSQL(query);
    }

    public void deleteData(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        Log.d("queryDelete", query);
        db.execSQL(query);
    }

    public ArrayList<Alarm> getData() {
        String[] columns = new String[]{COLUMN_ID, COLUMN_HOUR, COLUMN_MIN, COLUMN_DAYS, COLUMN_STATUS};
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        int iRow = c.getColumnIndex(COLUMN_ID);
        int iHour = c.getColumnIndex(COLUMN_HOUR);
        int iMin = c.getColumnIndex(COLUMN_MIN);
        int iDays = c.getColumnIndex(COLUMN_DAYS);
        int iDayChar = c.getColumnIndex(COLUMN_DAYS_CHAR);
        int iStatus = c.getColumnIndex(COLUMN_STATUS);

        arrInfo = new ArrayList<Alarm>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            alarm = new Alarm();
            alarm.setId(c.getInt(iRow));
            alarm.setHour(c.getString(iHour));
            alarm.setMin(c.getString(iMin));
            alarm.setRepeart(c.getString(iDays));
            alarm.setRepeartChar(c.getString(iDayChar));
            if (c.getString(iStatus).equals("true"))
                alarm.setStatus(true);
            else
                alarm.setStatus(false);
            arrInfo.add(alarm);

        }
        c.close();
        return arrInfo;
    }


    private static class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {
            String query = "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUR + " TEXT, "
                    + COLUMN_MIN + " TEXT, "
                    + COLUMN_DAYS + " TEXT, "
                    + COLUMN_DAYS_CHAR + " TEXT, "
                    + COLUMN_STATUS + " TEXT NOT NULL);";
            Log.d("query", query);
            arg0.execSQL(query);

        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(arg0);
        }
    }
}
