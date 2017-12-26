package com.globe.hand.Main.Tab1Map.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.model.MapEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 21..
 * Modified by SsangWoo on 2017. 12. 24..
 */

public class MapAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private ArrayList<MapEntity> mapEntityList = new ArrayList<>();

    public MapAdapter(Context mContext, ArrayList<MapEntity> mapEntityList) {
        this.mContext = mContext;
        this.mapEntityList = mapEntityList;
    }

    public MapAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MapViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ((MapViewHolder)holder).bindView(mContext,
                null/*mapEntityList.get(position)*/,
                position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
