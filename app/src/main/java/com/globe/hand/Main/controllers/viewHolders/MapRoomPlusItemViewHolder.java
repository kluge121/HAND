package com.globe.hand.Main.controllers.viewHolders;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globe.hand.Main.controllers.BaseViewHolder;
import com.globe.hand.R;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomPlusItemViewHolder extends BaseViewHolder {
    ImageView imageView;
    TextView textView;
    public MapRoomPlusItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.layout_map_room_item);
        imageView = itemView.findViewById(R.id.image_map_room);
        textView = itemView.findViewById(R.id.text_map_room_title);
    }

    @Override
    public void bindView(Object model, int position) {
        imageView.setImageResource(R.drawable.btn_x);
        textView.setText("추가해랏");
    }
}
