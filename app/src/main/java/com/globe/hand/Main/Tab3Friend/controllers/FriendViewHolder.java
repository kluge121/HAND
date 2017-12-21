package com.globe.hand.Main.Tab3Friend.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.R;


/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendViewHolder extends RecyclerView.ViewHolder{

    View v;
    ImageView profile;
    TextView name;
    TextView count;

    public FriendViewHolder(View itemView) {
        super(itemView);
        v = itemView;
//        profile = v.findViewById(R.id.tab3_item_profile);
//        name = v.findViewById(R.id.tab3_item_name);
//        count = v.findViewById(R.id.tab3_item_marker_count);
    }

    void setView(){

    }

}
