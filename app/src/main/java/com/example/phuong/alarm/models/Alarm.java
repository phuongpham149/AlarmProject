package com.example.phuong.alarm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phuong on 07/12/2016.
 */

public class Alarm implements Parcelable {
    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };
    private String mHour;
    private String mMin;
    private String mRepeart;
    private boolean mStatus;
    private String mRepeartChar;
    private int mId;

    protected Alarm(Parcel in) {
        mHour = in.readString();
        mMin = in.readString();
        mRepeart = in.readString();
        mStatus = in.readByte() != 0;
        mRepeartChar = in.readString();
        mId = in.readInt();
    }

    public Alarm(String mHour, String mMin, String mRepeart, boolean mStatus, String mRepeartChar) {
        this.mHour = mHour;
        this.mMin = mMin;
        this.mRepeart = mRepeart;
        this.mStatus = mStatus;
        this.mRepeartChar = mRepeartChar;
    }

    public Alarm(String mHour, String mMin, String mRepeart, boolean mStatus, String mRepeartChar, int mId) {
        this.mHour = mHour;
        this.mMin = mMin;
        this.mRepeart = mRepeart;
        this.mStatus = mStatus;
        this.mRepeartChar = mRepeartChar;
        this.mId = mId;
    }

    public Alarm() {
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String mHour) {
        this.mHour = mHour;
    }

    public String getMin() {
        return mMin;
    }

    public void setMin(String mMin) {
        this.mMin = mMin;
    }

    public String getRepeart() {
        return mRepeart;
    }

    public void setRepeart(String mRepeart) {
        this.mRepeart = mRepeart;
    }

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

    public String getRepeartChar() {
        return mRepeartChar;
    }

    public void setRepeartChar(String mRepeartChar) {
        this.mRepeartChar = mRepeartChar;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mHour);
        parcel.writeString(mMin);
        parcel.writeString(mRepeart);
        parcel.writeByte((byte) (mStatus ? 1 : 0));
        parcel.writeString(mRepeartChar);
        parcel.writeInt(mId);
    }
}
