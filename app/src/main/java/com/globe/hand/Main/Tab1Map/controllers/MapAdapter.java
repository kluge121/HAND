package com.globe.hand.Main.Tab1Map.controllers;

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


public class MapAdapter extends RecyclerView.Adapter<MapViewHolder> {

    private Context mContext;
    private ArrayList arrayList = new ArrayList();

    public MapAdapter(Context mContext, ArrayList arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    public MapAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_map, parent, false);
        return new MapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MapViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
