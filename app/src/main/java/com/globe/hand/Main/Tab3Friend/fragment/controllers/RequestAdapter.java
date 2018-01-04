package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.globe.hand.Main.Tab3Friend.model.RequestEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    private Context context;
    private List<DocumentSnapshot> friendRequsetmSnapshotList;


    public RequestAdapter(Context context, List<DocumentSnapshot> friendRequsetmSnapshotList) {
        this.friendRequsetmSnapshotList = friendRequsetmSnapshotList;
        this.context = context;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequestViewHolder(parent, R.layout.recycler_item_friend_request, this);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.bindView(context, friendRequsetmSnapshotList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return friendRequsetmSnapshotList.size();
    }

    public void removeItem(int position) {
        friendRequsetmSnapshotList.remove(position);
        notifyDataSetChanged();
    }



}
