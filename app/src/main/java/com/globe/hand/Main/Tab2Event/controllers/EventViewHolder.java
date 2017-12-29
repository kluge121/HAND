package com.globe.hand.Main.Tab2Event.controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventViewHolder extends BaseViewHolder<EventEntity> {

    private View view;



    EventViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        view = itemView;

    }


    @Override
    protected void bindView(Context context, EventEntity model, int position) {




    }
}
