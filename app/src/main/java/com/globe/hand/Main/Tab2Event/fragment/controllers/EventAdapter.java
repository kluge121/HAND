package com.globe.hand.Main.Tab2Event.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public void setArrayList(ArrayList<EventEntity> arrayList) {
        this.arrayList = arrayList;
    }

    private ArrayList<EventEntity> arrayList = new ArrayList<EventEntity>();
    private Context mContext;

    public EventAdapter(Context mContext) {
        arrayList = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new EventViewHolder(parent, R.layout.recycler_item_middle_event, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(mContext, arrayList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void removeItem(int position) {

        arrayList.remove(position);
        notifyDataSetChanged();
    }
}
