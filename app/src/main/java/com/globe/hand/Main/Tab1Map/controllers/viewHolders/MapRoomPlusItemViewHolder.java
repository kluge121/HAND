package com.globe.hand.Main.Tab1Map.controllers.viewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.hand.Main.Tab1Map.activities.InMapRoomActivity;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.R;
import com.globe.hand.enums.MapRoomPermission;
import com.globe.hand.models.JoinedMapRooms;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.MapRoomMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomPlusItemViewHolder extends BaseViewHolder {

    RelativeLayout mapRoomItemContainer;

    public MapRoomPlusItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_plus_item_map_room);
        mapRoomItemContainer = itemView.findViewById(R.id.map_room_item_container);
    }

    @Override
    public void bindView(final Context context, Object model, int position) {
        mapRoomItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 새로운 지도방 개설
                final EditText editMapRoomTitle = new EditText(context);
                final EditText editMapRoomDesc = new EditText(context);
                editMapRoomTitle.setHint("이름");
                editMapRoomDesc.setHint("설명");
                AlertDialog.Builder alertBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("새로운 방 개설")
                                .setMessage("방의 이름을 입력해주세요.")
                                .setPositiveButton(R.string.dialog_make, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!editMapRoomTitle.getText().toString().isEmpty()) {
                                            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                            final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            // 맵룸 생성
                                            MapRoom mapRoom = new MapRoom(
                                                    editMapRoomTitle.getText().toString(),
                                                    editMapRoomDesc.getText().toString());
                                            db.collection("map_room").add(mapRoom)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                String newMapRoomUid = task.getResult().getId();

                                                                DocumentReference newMapRoomRef
                                                                        = db.collection("map_room")
                                                                        .document(newMapRoomUid);

                                                                // UID 업데이트
                                                                newMapRoomRef.update("uid", newMapRoomUid);

                                                                // 자신의 db 정보
                                                                DocumentReference myUserRef
                                                                        = db.collection("user")
                                                                        .document(firebaseUser.getUid());

                                                                // 맵룸 멤버
                                                                DocumentReference mapRoomMembersRef =
                                                                        db.collection("map_room")
                                                                                .document(newMapRoomUid)
                                                                                .collection("members")
                                                                                .document(firebaseUser.getUid());

                                                                // 맴룹 멤버를 내 정보를 담아서 추가(어드민으로다가)
                                                                mapRoomMembersRef.set(new MapRoomMember(myUserRef,
                                                                        MapRoomPermission.ADMIN.name(),
                                                                        new Date()));

                                                                // 자신의 맵룸 리스트에 추가
                                                                db.collection("map_room").document(firebaseUser.getUid())
                                                                        .collection("joined_map_rooms").document(newMapRoomUid)
                                                                        .set(new JoinedMapRooms(newMapRoomRef));
                                                            }
                                                        }
                                                    });
                                        }
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editMapRoomTitle);
                linearLayout.addView(editMapRoomDesc);
                alertBuilder.setView(linearLayout);
                alertBuilder.show();
            }
        });
    }
}
