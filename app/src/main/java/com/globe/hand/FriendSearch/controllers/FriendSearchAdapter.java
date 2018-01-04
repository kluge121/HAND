package com.globe.hand.FriendSearch.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.CheckUser;
import com.globe.hand.models.User;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 31..
 */

public class FriendSearchAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private ArrayList<CheckUser> arrayList = new ArrayList();


    public FriendSearchAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setArrayList(ArrayList<CheckUser> arrayList) {
        this.arrayList = arrayList;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendSearchViewHolder(parent, R.layout.recycler_item_search_friend, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        ((FriendSearchViewHolder) holder).bindView(mContext, arrayList.get(position), position);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    void listUpdate(int position) {
        notifyItemChanged(position);
    }


}
