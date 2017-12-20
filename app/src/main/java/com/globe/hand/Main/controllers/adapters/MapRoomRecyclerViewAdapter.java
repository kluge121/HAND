package com.globe.hand.Main.controllers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.controllers.BaseViewHolder;
import com.globe.hand.Main.controllers.viewHolders.MapRoomItemViewHolder;
import com.globe.hand.Main.controllers.viewHolders.MapRoomPlusItemViewHolder;
import com.globe.hand.models.MapRoom;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private int MAP_ROOM_PLUS_VIEW_TYPE = 123;

    private List<MapRoom> mapRoomList;

    public MapRoomRecyclerViewAdapter(List<MapRoom> mapRoomList) {
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
            ((MapRoomItemViewHolder)holder).bindView(mapRoomList.get(position), position);
        } else {
            ((MapRoomPlusItemViewHolder)holder).bindView(null, position);
        }

    }

    @Override
    public int getItemCount() {
        return mapRoomList.size() + 1;
    }
}
