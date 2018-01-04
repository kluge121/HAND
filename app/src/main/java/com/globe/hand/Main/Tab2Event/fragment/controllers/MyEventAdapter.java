package com.globe.hand.Main.Tab2Event.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class MyEventAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    final int MY_EVENT = 111;


    private List<DocumentSnapshot> myEventSnapshotList;

    private Context mContext;

    public MyEventAdapter(List<DocumentSnapshot> myEventSnapshotList, Context mContext) {
        this.myEventSnapshotList = myEventSnapshotList;
        this.mContext = mContext;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new MyEventViewHolder(parent, R.layout.recycler_item_middle_event, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        holder.bindView(mContext, myEventSnapshotList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return myEventSnapshotList.size();
//        return arrayList.size();
    }

    public void removeItem(int position) {

        myEventSnapshotList.remove(position);
        notifyDataSetChanged();
    }
}
