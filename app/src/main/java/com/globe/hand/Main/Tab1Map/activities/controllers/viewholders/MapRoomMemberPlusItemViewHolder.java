package com.globe.hand.Main.Tab1Map.activities.controllers.viewholders;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.enums.MapRoomPermission;
import com.globe.hand.models.JoinedMapRooms;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.MapRoomMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoomMemberPlusItemViewHolder extends BaseViewHolder {

    public MapRoomMemberPlusItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.recycler_plus_item_map_room_member);
    }

    @Override
    public void bindView(final Context context, Object model, int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 지도방에 친구초대!
            }
        });
    }
}
