package com.example.phuong.alarm.adapters.RecyclerViewAlarmDeleteAdapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.phuong.alarm.R;
import com.example.phuong.alarm.listeners.RecyclerItemListener;
import com.example.phuong.alarm.models.Alarm;

import java.util.ArrayList;

/**
 * Created by phuong on 07/12/2016.
 */

public class RecyclerViewAlarmDeleteAdapter extends RecyclerView.Adapter<RecyclerViewAlarmDeleteAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Alarm> mAlarms;
    private RecyclerItemListener mItemListener;

    public RecyclerViewAlarmDeleteAdapter(ArrayList<Alarm> alarms, Context context, RecyclerItemListener listener) {
        mAlarms = alarms;
        mContext = context;
        mItemListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_recycler_delete, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Alarm alarm = mAlarms.get(position);
        holder.mTvTime.setText(alarm.getHour() + " : " + alarm.getMin());
        holder.mTvRepeat.setText(alarm.getRepeartChar());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;
        TextView mTvRepeat;
        CheckBox mTvStatus;

        public MyViewHolder(View view) {
            super(view);
            mTvTime = (TextView) view.findViewById(R.id.tvTimeAlarm);
            mTvRepeat = (TextView) view.findViewById(R.id.tvRepeat);
            mTvStatus = (CheckBox) view.findViewById(R.id.tvStatus);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemclick(getAdapterPosition());
                }
            });
            mTvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemChecked(getAdapterPosition(), mTvStatus.isChecked());
                }
            });
        }
    }
}
