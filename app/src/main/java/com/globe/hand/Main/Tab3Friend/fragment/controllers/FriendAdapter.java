package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<DocumentSnapshot> friendList;





    public FriendAdapter(Context context, List<DocumentSnapshot> documentSnapshots) {
        this.context = context;
        friendList = documentSnapshots;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new FriendViewHolder(parent, R.layout.recycler_item_friend, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(context, friendList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void remove(int position) {
        friendList.remove(position);
        notifyDataSetChanged();
    }


}
