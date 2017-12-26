package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.Main.Tab3Friend.model.FriendEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;


/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendViewHolder extends BaseViewHolder<FriendEntity>{

    private ImageView profile;
    private TextView name;
    private TextView count;

    FriendViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        profile = itemView.findViewById(R.id.tab3_item_profile);
        name = itemView.findViewById(R.id.tab3_item_name);
        count = itemView.findViewById(R.id.tab3_item_marker_count);
    }

    @Override
    protected void bindView(Context context, FriendEntity model, int position) {




    }

}
