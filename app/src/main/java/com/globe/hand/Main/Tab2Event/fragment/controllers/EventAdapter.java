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

    final int MY_EVENT = 111;

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
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if (viewType == MY_EVENT) {
//            return new MyEventViewHolder(parent, R.layout.recycler_item_middle_event);
//        }
        return new EventViewHolder(parent, R.layout.recycler_item_middle_event);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

//        if (arrayList.get(position).isParticipation()) {
//            ((ParticipationEventViewHolder) holder).bindView(mContext, arrayList.get(position), position);
//        } else {
//            ((EventViewHolder) holder).bindView(mContext, arrayList.get(position), position);
//        }


    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
//        if (arrayList.get(position).isParticipation()) {
//            return MY_EVENT;
//        } else {
//            return super.getItemViewType(position);
//        }
    }

    @Override
    public int getItemCount() {
        return 5;
//        return arrayList.size();
    }
}
