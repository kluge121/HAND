package com.globe.hand.Main.Tab2Event.fragment.controllers;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventViewHolder extends BaseViewHolder<EventEntity> {

    private View view;
    private TextView category;
    private TextView name;
    private TextView needCount;
    private TextView count;
    private TextView point;
    private TextView price;
    private TextView content;
    private ImageView image;


    EventViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        view = itemView;

        category = itemView.findViewById(R.id.event_item_category);
        name = itemView.findViewById(R.id.event_item_name);
        needCount = itemView.findViewById(R.id.event_item_need_count);
        count = itemView.findViewById(R.id.event_item_count);
        point = itemView.findViewById(R.id.event_item_point);
        price = itemView.findViewById(R.id.event_item_price);
        content = itemView.findViewById(R.id.event_item_content);
        image = itemView.findViewById(R.id.event_item_image);


    }


    @Override
    public void bindView(Context context, EventEntity model, int position) {

        category.setText(model.getCategory());
        name.setText(model.getEventName());
        needCount.setText(model.getNeedCount());
        count.setText(model.getCount());
        point.setText(model.getPoint());
        price.setText(model.getPrice());
        content.setText(model.getContent());

        if (model.getImageUrl() != null)
            Glide.with(context).load(Uri.parse(model.getImageUrl())).into(image);


    }
}
