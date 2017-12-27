package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab3Friend.model.FriendEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;


/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendViewHolder extends BaseViewHolder<User>{

    private ImageView profile;
    private TextView name;

    FriendViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        profile = itemView.findViewById(R.id.tab3_item_profile);
        name = itemView.findViewById(R.id.tab3_item_name);
    }

    @Override
    protected void bindView(Context context, User model, int position) {

        name.setText(model.getName());
        if(model.getProfile_url()!=null){
            Glide.with(context).load(model.getProfile_url()).into(profile);
        }

    }
}
