package com.globe.hand.Main.Tab4Alarm.controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe.hand.Main.Tab4Alarm.models.AlarmEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class AlarmViewHolder extends BaseViewHolder<AlarmEntity>{

    View v;
    CircleImageView profile;
    TextView name;
    TextView content;

    public AlarmViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        v = itemView;
        profile = v.findViewById(R.id.alarm_item_profile);
        name = v.findViewById(R.id.alarm_item_name);
        content = v.findViewById(R.id.alarm_item_content);

    }

    @Override
    protected void bindView(Context context, AlarmEntity model, int position) {


    }
}
