package com.globe.hand.MapRoom.controllers.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe.hand.MapRoom.RealMapActivity;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomItemViewHolder extends BaseViewHolder<MapRoom> {

    LinearLayout mapRoomItemContainer;
    ImageView imageMapRoom;
    TextView textMapRoomTitle;

    public MapRoomItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_map_room);
        mapRoomItemContainer = itemView.findViewById(R.id.map_room_item_container);
        imageMapRoom = itemView.findViewById(R.id.image_map_room);
        textMapRoomTitle = itemView.findViewById(R.id.text_map_room_title);
    }

    @Override
    public void bindView(final Context context, final MapRoom model, int position) {
        mapRoomItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RealMapActivity.class);
                intent.putExtra("map_room_id", model.getId());
                context.startActivity(intent);
            }
        });
        imageMapRoom.setImageResource(model.getPicturePath());
        textMapRoomTitle.setText(model.getTitle());
    }
}
