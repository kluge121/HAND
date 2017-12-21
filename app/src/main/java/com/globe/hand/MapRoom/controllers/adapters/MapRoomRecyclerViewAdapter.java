package com.globe.hand.MapRoom.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.MapRoom.controllers.BaseViewHolder;
import com.globe.hand.MapRoom.controllers.viewHolders.MapRoomItemViewHolder;
import com.globe.hand.MapRoom.controllers.viewHolders.MapRoomPlusItemViewHolder;
import com.globe.hand.models.MapRoom;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private int MAP_ROOM_PLUS_VIEW_TYPE = 123;

    private Context context;

    private List<MapRoom> mapRoomList;

    public MapRoomRecyclerViewAdapter(Context context, List<MapRoom> mapRoomList) {
        this.context = context;
        this.mapRoomList = mapRoomList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MAP_ROOM_PLUS_VIEW_TYPE) {
            return new MapRoomPlusItemViewHolder(parent);
        } else {
            return new MapRoomItemViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mapRoomList.size()) {
            return super.getItemViewType(position);
        } else {
            return MAP_ROOM_PLUS_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(position < mapRoomList.size()) {
            ((MapRoomItemViewHolder)holder).bindView(context,
                    mapRoomList.get(position), position);
        } else {
            ((MapRoomPlusItemViewHolder)holder).bindView(context,
                    null, position);
        }

    }

    @Override
    public int getItemCount() {
        return mapRoomList.size() + 1;
    }
}
