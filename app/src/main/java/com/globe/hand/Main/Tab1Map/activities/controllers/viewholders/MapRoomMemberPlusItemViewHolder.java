package com.globe.hand.Main.Tab1Map.activities.controllers.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.activities.InviteMemberActivity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomMemberPlusItemViewHolder extends BaseViewHolder<String> {

    public MapRoomMemberPlusItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_plus_item_map_room_member);
    }

    @Override
    public void bindView(final Context context, final String mapRoomUid, int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 지도방에 친구초대!
                Intent inviteMemberIntent = new Intent(context, InviteMemberActivity.class);
                inviteMemberIntent.putExtra("map_room_uid", mapRoomUid);
                context.startActivity(inviteMemberIntent);
            }
        });
    }
}
