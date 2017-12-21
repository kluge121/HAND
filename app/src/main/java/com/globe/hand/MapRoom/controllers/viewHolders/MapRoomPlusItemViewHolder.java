package com.globe.hand.MapRoom.controllers.viewHolders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomPlusItemViewHolder extends BaseViewHolder {

    LinearLayout mapRoomItemContainer;
    ImageView imageView;
    TextView textView;

    public MapRoomPlusItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.layout_map_room_item);
        mapRoomItemContainer = itemView.findViewById(R.id.map_room_item_container);
        imageView = itemView.findViewById(R.id.image_map_room);
        textView = itemView.findViewById(R.id.text_map_room_title);
    }

    @Override
    public void bindView(final Context context, Object model, int position) {
//        mapRoomItemContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO : 새로운 지도방 개설
//                Intent intent = new Intent(context, RealMapActivity.class);
//                context.startActivity(intent);
//            }
//        });
        imageView.setImageResource(R.drawable.btn_x);
        textView.setText("추가해랏");
    }
}
