package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.globe.hand.Main.Tab3Friend.model.RequestEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    private Context context;
    private ArrayList<User> arrayList = new ArrayList<>();

    public void setArrayList(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }

    public RequestAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequestViewHolder(parent, R.layout.recycler_item_friend_request, this);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.bindView(context, arrayList.get(position), position);
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
