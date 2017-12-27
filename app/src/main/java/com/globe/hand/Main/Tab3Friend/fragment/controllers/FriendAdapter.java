package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private Context context;
    private ArrayList<User> arrayList = new ArrayList<>();

    public void setArrayList(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }

    public FriendAdapter(Context context) {
        this.context = context;
    }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendViewHolder(parent, R.layout.recycler_item_friend);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bindView(context, arrayList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}