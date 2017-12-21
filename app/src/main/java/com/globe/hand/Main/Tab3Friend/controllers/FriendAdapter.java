package com.globe.hand.Main.Tab3Friend.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {


    private Context mContext;
    private ArrayList arrayList = new ArrayList();

    public FriendAdapter(Context mContext, ArrayList arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public FriendAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_friend,parent,false);
        return new FriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
