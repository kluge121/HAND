package com.globe.hand.Main.Tab1Map.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.controllers.viewHolders.MapRoomFirebaseItemViewHolder;
import com.globe.hand.Main.Tab1Map.controllers.viewHolders.MapRoomItemViewHolder;
import com.globe.hand.Main.Tab1Map.controllers.viewHolders.MapRoomPlusItemViewHolder;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.MapRoom;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomFirebaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private int MAP_ROOM_PLUS_VIEW_TYPE = 123;

    private Context context;

    private List<DocumentSnapshot> mapRoomSnapshotList;

    public MapRoomFirebaseRecyclerViewAdapter(Context context,
                                              List<DocumentSnapshot> mapRoomSnapshotList) {
        this.context = context;
        this.mapRoomSnapshotList = mapRoomSnapshotList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MAP_ROOM_PLUS_VIEW_TYPE) {
            return new MapRoomPlusItemViewHolder(parent);
        } else {
            return new MapRoomFirebaseItemViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mapRoomSnapshotList.size()) {
            return super.getItemViewType(position);
        } else {
            return MAP_ROOM_PLUS_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(position < mapRoomSnapshotList.size()) {
            ((MapRoomFirebaseItemViewHolder)holder).bindView(context,
                    mapRoomSnapshotList.get(position), position);
        } else {
            ((MapRoomPlusItemViewHolder)holder).bindView(context,
                    null, position);
        }

    }

    @Override
    public int getItemCount() {
        return mapRoomSnapshotList.size() + 1;
    }
}
