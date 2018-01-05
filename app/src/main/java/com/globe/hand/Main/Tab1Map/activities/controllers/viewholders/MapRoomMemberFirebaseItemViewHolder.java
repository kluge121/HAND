package com.globe.hand.Main.Tab1Map.activities.controllers.viewholders;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.MapRoomMember;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomMemberFirebaseItemViewHolder extends BaseViewHolder<MapRoomMember> {

    private ImageView imageMemberPhoto;
    private TextView textMemberName;

    public MapRoomMemberFirebaseItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_item_map_room_member);
        imageMemberPhoto = itemView.findViewById(R.id.image_map_room_member_photo);
        textMemberName = itemView.findViewById(R.id.text_map_room_member_name);
    }

    @Override
    public void bindView(final Context context,
                         final MapRoomMember member, final int position) {
        final DocumentReference userReference = member.getUserReference();
        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final User user = task.getResult().toObject(User.class);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO : 친구정보 보여주기
                        }
                    });

                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(!user.getUid().equals(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                new AlertDialog.Builder(context)
                                        .setTitle("경고")
                                        .setMessage(user.getName() + "를 이 방에서 추방 하시겠습니까?")
                                        .setPositiveButton(context.getString(R.string.dialog_kick),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FirebaseFirestore.getInstance()
                                                                .collection("map_room")
                                                                .document(member.getMapRoomUid())
                                                                .collection("members")
                                                                .document(member.getUserReference().getId())
                                                                .delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            member.getUserReference()
                                                                                    .collection("joined_map_rooms")
                                                                                    .document(member.getMapRoomUid())
                                                                                    .delete();
                                                                        }
                                                                    }
                                                                });
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

                    if (user.getProfile_url() != null) {
                        Glide.with(context)
                                .load(user.getProfile_url())
                                .into(imageMemberPhoto);
                    }
                    textMemberName.setText(user.getName());
                }
            }
        });
    }
}