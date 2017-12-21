package com.globe.hand.Main.Tab1Map.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.Main.Tab1Map.model.MapEntity;
import com.globe.hand.R;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class MapViewHolder extends RecyclerView.ViewHolder {

    private View v;
    private ImageView imageView;
    private TextView textView;

    MapViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        imageView = itemView.findViewById(R.id.tab1_item_imageview);
        textView = itemView.findViewById(R.id.tab1_item_textview);
    }

    void setView(MapEntity data){
        // 글라이드를 통해 이미지 가져옮 imageView
    }
}
