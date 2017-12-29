package com.globe.hand.Main.Tab2Event.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.R;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    public void setArrayList(ArrayList<EventEntity> arrayList) {
        this.arrayList = arrayList;
    }

    private ArrayList<EventEntity> arrayList;
    private Context mContext;

    public EventAdapter(Context mContext) {
        arrayList = new ArrayList<>();
        arrayList.add(new EventEntity());
        this.mContext = mContext;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(parent, R.layout.recycler_item_event);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
//        holder.bindView(mContext, arrayList.get(position), position);


    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
