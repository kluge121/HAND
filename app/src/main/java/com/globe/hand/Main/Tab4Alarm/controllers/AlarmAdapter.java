package com.globe.hand.Main.Tab4Alarm.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab4Alarm.models.AlarmEntity;
import com.globe.hand.R;
import com.globe.hand.models.Notification;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private Context mContext;
    private List<DocumentSnapshot> notiSnapshotList;


    public AlarmAdapter(Context mContext, List<DocumentSnapshot> notiSnapshotList) {

        this.mContext = mContext;
        this.notiSnapshotList = notiSnapshotList;

    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AlarmViewHolder(parent, R.layout.recycler_item_alarm, this);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {

        holder.bindView(mContext, notiSnapshotList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return notiSnapshotList.size();
    }

    public void removeItem(int position) {
        notiSnapshotList.remove(position);
        notifyDataSetChanged();
    }
}
