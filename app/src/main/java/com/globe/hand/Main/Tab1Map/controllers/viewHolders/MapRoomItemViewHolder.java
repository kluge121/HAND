package com.globe.hand.Main.Tab1Map.controllers.viewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.Main.Tab1Map.activities.InMapRoomActivity;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
                intent.putExtra("map_room_uid", model.getUid());
                context.startActivity(intent);
            }
        });




        RequestOptions requestOptions
                = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(25));

        if (model.getPicturePath() != null) {
            Glide.with(context)
                    .load(model.getPicturePath())
                    .apply(requestOptions)
                    .into(imageMapRoom);
        } else {
            Glide.with(context)
                    .load(R.drawable.hand_splash)
                    .apply(requestOptions)
                    .into(imageMapRoom);
        }
        textMapRoomTitle.setText(model.getTitle());
        textMapRoomDesc.setText(model.getDesc());


    }
}
