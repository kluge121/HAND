package com.globe.hand.Main.Tab1Map.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe.hand.Main.Tab1Map.model.MapEntity;
import com.globe.hand.MapRoom.RealMapActivity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class MapViewHolder extends BaseViewHolder<MapEntity> {

    private LinearLayout container;
    private ImageView imageView;
    private TextView textView;

    MapViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_map);
        container = itemView.findViewById(R.id.map_view_holder_container);
        imageView = itemView.findViewById(R.id.tab1_item_imageview);
        textView = itemView.findViewById(R.id.tab1_item_textview);
    }

    @Override
    protected void bindView(final Context context, MapEntity model, int position) {
        // TODO: 글라이드로 이미지 바꾸기
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RealMapActivity.class));
            }
        });
    }
}
