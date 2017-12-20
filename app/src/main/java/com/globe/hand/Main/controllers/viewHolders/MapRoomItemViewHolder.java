package com.globe.hand.Main.controllers.viewHolders;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.Main.controllers.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomItemViewHolder extends BaseViewHolder<MapRoom> {

    ImageView imageMapRoom;
    TextView textMapRoomTitle;

    public MapRoomItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.layout_map_room_item);
        imageMapRoom = itemView.findViewById(R.id.image_map_room);
        textMapRoomTitle = itemView.findViewById(R.id.text_map_room_title);
    }

    @Override
    public void bindView(MapRoom model, int position) {
        imageMapRoom.setImageResource(model.getPicturePath());
        textMapRoomTitle.setText(model.getTitle());
    }
}
