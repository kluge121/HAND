package com.globe.hand.FriendSearch.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.common.GetLoginUserEntity;
import com.globe.hand.enums.NotificationType;
import com.globe.hand.models.CheckUser;
import com.globe.hand.Main.Tab4Alarm.models.Notification;
import com.globe.hand.models.UploadUser;
import com.globe.hand.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by baeminsu on 2017. 12. 31..
 */

public class FriendSearchViewHolder extends BaseViewHolder<CheckUser> {

    private CircleImageView profile;
    private TextView name;
    private TextView email;
    private ImageButton button;

    private FirebaseFirestore db;
    private DocumentReference requestRef;
    private DocumentReference responseRef;
    private DocumentReference responseNoRef;

    public final int ALREADY_REQUEST = 0;
    public final int ALREADY_FRIEND = 1;
    public final int NOMAL = 2;

    private FriendSearchAdapter adapter;
    private Handler handler;

    FriendSearchViewHolder(ViewGroup parent, int layoutId, FriendSearchAdapter adapter) {
        super(parent, layoutId);

        profile = itemView.findViewById(R.id.search_friend_item_profile);
        name = itemView.findViewById(R.id.search_friend_item_name);
        email = itemView.findViewById(R.id.search_friend_item_email);
        button = itemView.findViewById(R.id.search_friend_item_button);
        this.adapter = adapter;
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void bindView(Context context, final CheckUser model, int position) {

        //TODO 모델데이터 출력
        if (model.getProfile_url() != null)
            Glide.with(context).load(model.getProfile_url()).into(profile);

        name.setText(model.getName());
        email.setText(model.getEmail());

        if (model.getState() == NOMAL) {
            button.setImageResource(R.drawable.friend_plus);
        } else if (model.getState() == ALREADY_REQUEST) {
            button.setImageResource(R.drawable.friend_x);
        } else {
            button.setVisibility(View.INVISIBLE);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getState() == NOMAL) {
                    requserAddFriend(model);
                } else if (model.getState() == ALREADY_REQUEST) {
                    cancelAddFriend(model);
                }
            }
        });


    }


    //notification
    private void requserAddFriend(final CheckUser model) {

        User loginUser = GetLoginUserEntity.makeLoginUserInstance(); // 요거는 나
        User searchUser = model; //이거는 내가 추가하고 싶은 사람

        friendRefSetting(loginUser, searchUser);


        Notification notification = new Notification();

        notification.setProfile_url(loginUser.getProfile_url());
        notification.setContent(loginUser.getName() + "님이 친구신청을 하였습니다.");
        notification.setNotiType(NotificationType.FRIEND_REQUEST.name());
        notification.setDate(new Date());
        notification.setSendUser(loginUser.getName());
        notification.setAdditionalInformation(loginUser.getUid());


        notification.setCheckNoti(false);


        responseNoRef = db.collection("user").document(searchUser.getUid())
                .collection("notification").document();


        WriteBatch batch = db.batch();

        batch.set(requestRef, new UploadUser(searchUser));
        batch.set(responseRef, new UploadUser(loginUser));
        batch.set(responseNoRef, notification);


        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    model.setState(ALREADY_REQUEST);
                    handler.post(new ListUpdateAction());

                }
            }
        });


    }

    private void cancelAddFriend(final CheckUser model) {

        User loginUser = GetLoginUserEntity.makeLoginUserInstance(); // 요거는 나
        User searchUser = model; //이거는 내가 추가하고 싶은 사람

        friendRefSetting(loginUser, searchUser);

        WriteBatch batch = db.batch();

        batch.delete(requestRef);
        batch.delete(responseRef);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    model.setState(NOMAL);
                    handler.post(new ListUpdateAction());

                }
            }
        });
    }


    private void friendRefSetting(User loginUser, User searchUser) {
        db = FirebaseFirestore.getInstance();

        requestRef = db.collection("user").document(loginUser.getUid()).
                collection("reqeustFriend").document(searchUser.getUid());

        responseRef = db.collection("user").document(searchUser.getUid()).
                collection("responseFriend").document(loginUser.getUid());


    }

    class ListUpdateAction implements Runnable {
        @Override
        public void run() {
            adapter.listUpdate(getAdapterPosition());
        }
    }

}
