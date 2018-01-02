package com.globe.hand.Main.Tab4Alarm.controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    public void bindView(Context context, AlarmEntity model, int position) {

        if (model.getProfile_url()!=null)
            Glide.with(context).load(model.getProfile_url()).into(profile);

        name.setText(model.getName());
        content.setText(model.getCotent());


    }
}
