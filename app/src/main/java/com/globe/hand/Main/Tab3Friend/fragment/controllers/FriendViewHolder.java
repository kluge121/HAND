package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab3Friend.model.FriendEntity;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.common.GetLoginUserEntity;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendViewHolder extends BaseViewHolder<DocumentSnapshot> {

    private CircleImageView profile;
    private TextView name;
    private TextView email;
    private ImageButton btn;
    private FriendAdapter adapter;
    private User friendUser;

    FriendViewHolder(ViewGroup parent, int layoutId, FriendAdapter adapter) {
        super(parent, layoutId);
        profile = itemView.findViewById(R.id.tab3_item_profile);
        name = itemView.findViewById(R.id.tab3_item_name);
        email = itemView.findViewById(R.id.tab3_item_email);
        btn = itemView.findViewById(R.id.tab3_item_button);
        this.adapter = adapter;
    }

    @Override
    public void bindView(final Context context, final DocumentSnapshot documentSnapshot, final int position) {

        final DocumentReference friendUserRef =
                (DocumentReference) documentSnapshot.get("userRef");


        friendUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                friendUser = task.getResult().toObject(User.class);

                if (task.isSuccessful()) {
                    name.setText(friendUser.getName());
                    if (friendUser.getProfile_url() != null) {
                        Glide.with(context).load(friendUser.getProfile_url()).into(profile);
                    }
                    email.setText(friendUser.getEmail());



                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(adapter.getContext())
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            removeFriend(GetLoginUserEntity.makeLoginUserInstance(), friendUser, position);

                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setMessage(friendUser.getName() + "님을 정말 삭제하시겠습니까")
                                    .create().show();
                        }
                    });
                }

            }
        });


    }


    //requestUser -> 삭제 신청한 사람
    //responseUser -> 어리둥절하다 삭제당하는 사람 (실제 응답은 없지만 그냥 이름을 이렇게 지음)
    private void removeFriend(User requestUser, User responseUser, final int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();

        DocumentReference requsetUserFriend = db.collection("user").document(requestUser.getUid())
                .collection("friend").document(responseUser.getUid());

        DocumentReference responseUserFriend = db.collection("user").document(responseUser.getUid())
                .collection("friend").document(requestUser.getUid());

        batch.delete(requsetUserFriend);
        batch.delete(responseUserFriend);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    adapter.remove(position);
                }
            }
        });
    }

}
