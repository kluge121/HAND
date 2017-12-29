package com.globe.hand.Main.Tab4Alarm.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab4Alarm.models.AlarmEntity;
import com.globe.hand.R;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private Context mContext;
    private ArrayList<AlarmEntity> arrayList = new ArrayList<>();


    public AlarmAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<AlarmEntity> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AlarmViewHolder(parent, R.layout.recycler_item_alarm);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
//        holder.bindView(mContext,arrayList.get(position),position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
