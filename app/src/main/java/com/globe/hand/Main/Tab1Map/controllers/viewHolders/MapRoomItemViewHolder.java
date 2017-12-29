package com.globe.hand.Main.Tab1Map.controllers.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab1Map.activities.InMapRoomActivity;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomItemViewHolder extends BaseViewHolder<MapRoom> {

    RelativeLayout mapRoomItemContainer;
    ImageView imageMapRoom;
    TextView textMapRoomTitle;
    TextView textMapRoomDesc;

    public MapRoomItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_map_room);
        mapRoomItemContainer = itemView.findViewById(R.id.map_room_item_container);
        imageMapRoom = itemView.findViewById(R.id.image_map_room);
        textMapRoomTitle = itemView.findViewById(R.id.text_map_room_title);
        textMapRoomDesc = itemView.findViewById(R.id.text_map_room_desc);
    }

    @Override
    public void bindView(final Context context, final MapRoom model, int position) {
        mapRoomItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InMapRoomActivity.class);
                intent.putExtra("map_room_id", model.getUid());
                context.startActivity(intent);
            }
        });

        if(model.getPicturePath() != null) {
            Glide.with(imageMapRoom)
                    .load(model.getPicturePath())
                    .submit();
        }
        textMapRoomTitle.setText(model.getTitle());
        textMapRoomDesc.setText(model.getDesc());
    }
}
