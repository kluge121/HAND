package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    Context context;
    ArrayList arrayList;

    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendViewHolder(parent, R.layout.recycler_item_friend);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
