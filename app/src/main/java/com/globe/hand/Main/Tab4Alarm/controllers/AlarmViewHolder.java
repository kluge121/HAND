package com.globe.hand.Main.Tab4Alarm.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab1Map.activities.InMapRoomActivity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.Main.Tab4Alarm.models.Notification;
import com.globe.hand.enums.NotificationType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class AlarmViewHolder extends BaseViewHolder<DocumentSnapshot> {

    private View v;
    private CircleImageView profile;
    private TextView content;
    private ImageButton closeBtn;
    private AlarmAdapter adapter;
    private ImageView checkIv;
    private TextView dateTv;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public AlarmViewHolder(ViewGroup parent, int layoutId, AlarmAdapter adapter) {
        super(parent, layoutId);
        v = itemView;
        profile = v.findViewById(R.id.alarm_item_profile);
        content = v.findViewById(R.id.alarm_item_content);
        closeBtn = v.findViewById(R.id.alarm_item_delete);
        checkIv = v.findViewById(R.id.alarm_item_noti_check_iv);
        dateTv = v.findViewById(R.id.alarm_item_date);


        this.adapter = adapter;
    }

    @Override
    public void bindView(final Context context, final DocumentSnapshot snapshot, final int position) {

        final Notification notification = snapshot.toObject(Notification.class);

        if (notification.getProfile_url() != null)
            Glide.with(context).load(notification.getProfile_url()).into(profile);
        content.setText(notification.getContent());

        if (!notification.isCheckNoti()) {
            checkIv.setVisibility(View.VISIBLE);
        }


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateTv.setText(dateFormat.format(notification.getDate()));


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("user").document(user.getUid()).
                        collection("notification").document(snapshot.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            adapter.removeItem(position);
                        }
                    }
                });


            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!notification.isCheckNoti()) {
                    db.collection("user").document(user.getUid())
                            .collection("notification").document(snapshot.getId())
                            .update("checkNoti", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                checkIv.setVisibility(View.INVISIBLE);


                            } else {
                                Toast.makeText(context, "네트워크상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                switch (notification.getNotiType()) {

                    case "MAPROOM_INVITE":
                        String mapRoomUid = notification.getAdditionalInformation();
                        Intent intent1 = new Intent(context, InMapRoomActivity.class);
                        intent1.putExtra("map_room_uid", mapRoomUid);
                        context.startActivity(intent1);
                        break;


                    case "FRIEND_REQUEST":
                        String userUid = notification.getAdditionalInformation();
                        Intent intent2 = new Intent(context, InMapRoomActivity.class);
                        intent2.putExtra("map_room_uid", userUid);
                        intent2.putExtra("friend_name", notification.getSendUser());
                        context.startActivity(intent2);

                    case "FRIEND_RESPONSE":
                        String userUid2 = notification.getAdditionalInformation();
                        Intent intent3 = new Intent(context, InMapRoomActivity.class);
                        intent3.putExtra("map_room_uid", userUid2);
                        intent3.putExtra("friend_name", notification.getSendUser());
                        context.startActivity(intent3);

                }

            }
        });


    }
}
