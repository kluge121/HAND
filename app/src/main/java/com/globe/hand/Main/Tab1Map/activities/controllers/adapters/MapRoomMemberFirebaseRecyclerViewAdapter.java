package com.globe.hand.Main.Tab1Map.activities.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.activities.controllers.viewholders.MapRoomMemberFirebaseItemViewHolder;
import com.globe.hand.Main.Tab1Map.activities.controllers.viewholders.MapRoomMemberPlusItemViewHolder;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.MapRoomMember;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomMemberFirebaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private int MAP_ROOM_FRIEND_PLUS_VIEW_TYPE = 123;

    private Context context;

    private List<MapRoomMember> mapRoomMemberList;

    public MapRoomMemberFirebaseRecyclerViewAdapter(Context context,
                                                    List<MapRoomMember> mapRoomMemberList) {
        this.context = context;
        this.mapRoomMemberList = mapRoomMemberList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MAP_ROOM_FRIEND_PLUS_VIEW_TYPE) {
            return new MapRoomMemberPlusItemViewHolder(parent);
        } else {
            return new MapRoomMemberFirebaseItemViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mapRoomMemberList.size()) {
            return super.getItemViewType(position);
        } else {
            return MAP_ROOM_FRIEND_PLUS_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(position < mapRoomMemberList.size()) {
            ((MapRoomMemberFirebaseItemViewHolder)holder).bindView(context,
                    mapRoomMemberList.get(position), position);
        } else {
            ((MapRoomMemberPlusItemViewHolder)holder).bindView(context,
                    mapRoomMemberList.get(0).getMapRoomUid(), position);
        }

    }

    @Override
    public int getItemCount() {
        return mapRoomMemberList.size() + 1;
    }
}
