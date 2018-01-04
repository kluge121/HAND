package com.globe.hand.Main.Tab1Map.controllers.viewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.Main.Tab1Map.activities.InMapRoomActivity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.MapRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomFirebaseItemViewHolder extends BaseViewHolder<DocumentSnapshot> {

    private RelativeLayout mapRoomItemContainer;
    private ImageView imageMapRoom;
    private TextView textMapRoomTitle;
    private TextView textMapRoomDesc;

    public MapRoomFirebaseItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_map_room);
        mapRoomItemContainer = itemView.findViewById(R.id.map_room_item_container);
        imageMapRoom = itemView.findViewById(R.id.image_map_room);
        textMapRoomTitle = itemView.findViewById(R.id.text_map_room_title);
        textMapRoomDesc = itemView.findViewById(R.id.text_map_room_desc);
    }

    @Override
    public void bindView(final Context context,
                         final DocumentSnapshot documentSnapshot, final int position) {

        final DocumentReference mapRoomReference =
                (DocumentReference) documentSnapshot.get("mapRoomReference");



        mapRoomReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final MapRoom mapRoom = task.getResult().toObject(MapRoom.class);

                    mapRoomItemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, InMapRoomActivity.class);
                            intent.putExtra("map_room_uid", mapRoom.getUid());
                            context.startActivity(intent);
                        }
                    });

                    mapRoomItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(mapRoom.getUid().equals(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                new AlertDialog.Builder(context)
                                        .setTitle("경고")
                                        .setMessage(mapRoom.getTitle() + " 지도방을 삭제하시겠습니까?")
                                        .setPositiveButton(context.getString(R.string.dialog_delete),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                        db.collection("map_room").document(mapRoom.getUid())
                                                                .delete();
                                                        db.collection("map_room").document(
                                                                FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .collection("joined_map_rooms")
                                                                .document(mapRoom.getUid()).delete();
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                        .setNegativeButton(context.getString(R.string.dialog_cancel),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).show();
                                return true;
                            }
                            return false;
                        }
                    });

                    RequestOptions requestOptions
                            = new RequestOptions().transforms(new CenterCrop(),
                            new RoundedCorners(25));

                    if (mapRoom.getPicturePath() != null) {
                        Glide.with(context)
                                .load(mapRoom.getPicturePath())
                                .apply(requestOptions)
                                .into(imageMapRoom);
                    } else {
                        Glide.with(context)
                                .load(R.drawable.hand_splash)
                                .apply(requestOptions)
                                .into(imageMapRoom);
                    }
                    textMapRoomTitle.setText(mapRoom.getTitle());
                    textMapRoomDesc.setText(mapRoom.getDesc());
                }
            }
        });
    }
}