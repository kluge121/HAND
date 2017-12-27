package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class RequestViewHolder extends BaseViewHolder<User> {


    private RequestAdapter adapter;

    private User requestUser;
    private FirebaseUser loginUser;
    private FirebaseFirestore db;

    private ImageView profile;
    private TextView name;
    public Button btnAccept;
    public Button btnReject;

    private DocumentReference requestRef;
    private DocumentReference responseRef;

    private DocumentReference addFriendRes;
    private DocumentReference addFriendReq;

    Handler handler;


    RequestViewHolder(ViewGroup parent, int layoutId, RequestAdapter adapter) {
        super(parent, layoutId);

        this.adapter = adapter;
        btnAccept = itemView.findViewById(R.id.friend_request_item_accept_btn);
        btnReject = itemView.findViewById(R.id.friend_request_item_reject_btn);
        profile = itemView.findViewById(R.id.friend_request_item_profile);
        name = itemView.findViewById(R.id.friend_request_item_name);

        db = FirebaseFirestore.getInstance();
        loginUser = FirebaseAuth.getInstance().getCurrentUser();
        handler = new Handler(Looper.getMainLooper());


    }

    @Override
    protected void bindView(final Context context, User model, final int position) {

        friendRefSetting(model);
        requestUser = model;


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new AcceptRunnable()).start();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new RejectRunnable()).start();
            }
        });


        if (model.getProfile_url() != null) {
            Glide.with(context).load(model.getProfile_url()).into(profile);
        }
        name.setText(model.getName());

    }


    private void friendRefSetting(User model) {


        //모델 - 요청을 한 사람 정보

        //요청을 한 사람
        requestRef = db.collection("user").document(model.getUid()).
                collection("reqeustFriend").document(loginUser.getUid());

        //요청을 받은 사람
        responseRef = db.collection("user").document(loginUser.getUid()).
                collection("responseFriend").document(model.getUid());


        //요청한 사람에 추가 (즉 응답한사람 모델 정보 추가
        addFriendRes = db.collection("user").document(model.getUid()).
                collection("friend").document(loginUser.getUid());

        //요청 받은 사람에 추가 (즉 요청한 사람의 모델정보 추가
        addFriendReq = db.collection("user").document(loginUser.getUid()).
                collection("friend").document(model.getUid());

    }


    private void rejectRequest() {

        WriteBatch batch = db.batch();
        batch.delete(requestRef);
        batch.delete(responseRef);
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handler.post(new ListUpdateAction());
            }
        });


    }

    private void acceptRequest() {
        WriteBatch batch = db.batch();

        batch.set(addFriendRes, makeLoginUserInstance());
        batch.set(addFriendReq, requestUser);

        batch.delete(requestRef);
        batch.delete(responseRef);

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handler.post(new ListUpdateAction());
            }
        });

    }


    User makeLoginUserInstance() {
        User meUser = new User();
        meUser.setUid(loginUser.getUid());
        meUser.setEmail(loginUser.getEmail());
        meUser.setName(loginUser.getDisplayName());
        meUser.setGender(null);
        if (loginUser.getPhotoUrl() != null)
            meUser.setProfile_url(loginUser.getPhotoUrl());

        return meUser;
    }


    class AcceptRunnable implements Runnable {
        @Override
        public void run() {
            acceptRequest();
        }
    }

    class RejectRunnable implements Runnable {
        @Override
        public void run() {
            rejectRequest();
        }
    }

    class ListUpdateAction implements Runnable {
        @Override
        public void run() {
            adapter.removeItem(getAdapterPosition());
        }
    }


}
