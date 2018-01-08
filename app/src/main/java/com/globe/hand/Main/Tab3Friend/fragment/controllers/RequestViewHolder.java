package com.globe.hand.Main.Tab3Friend.fragment.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab3Friend.fragment.FriendList;
import com.globe.hand.PhotoPreview.ProfilePhotoPreView;
import com.globe.hand.R;
import com.globe.hand.common.BaseViewHolder;
import com.globe.hand.enums.NotificationType;
import com.globe.hand.models.Notification;
import com.globe.hand.models.UploadUser;
import com.globe.hand.models.User;
import com.globe.hand.temp.AdapterTempStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by baeminsu on 2017. 12. 26..
 */

public class RequestViewHolder extends BaseViewHolder<DocumentSnapshot> {


    private RequestAdapter adapter;

    private User requestUser;
    private FirebaseUser loginUser;
    private FirebaseFirestore db;

    private CircleImageView profile;
    private TextView name;
    private Button btnAccept;
    private Button btnReject;

    private DocumentReference requestRef;
    private DocumentReference responseRef;

    private DocumentReference addFriendRes;
    private DocumentReference addFriendReq;

    private DocumentReference responseNotiRef;


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
    public void bindView(final Context context, final DocumentSnapshot documentSnapshot, final int position) {


        final DocumentReference requestUserRef =
                (DocumentReference) documentSnapshot.get("userRef");

        requestUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                requestUser = task.getResult().toObject(User.class);
                friendRefSetting(requestUser);


                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new AcceptRunnable(position)).start();
                    }
                });


                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new RejectRunnable(position)).start();
                    }
                });


                if (requestUser.getProfile_url() != null) {
                    Glide.with(context).load(requestUser.getProfile_url()).into(profile);
                }
                name.setText(requestUser.getName());

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user").document(requestUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User tmp = task.getResult().toObject(User.class);

                            if (tmp.getProfile_url() != null) {
                                Intent intent = new Intent(context, ProfilePhotoPreView.class);
                                intent.putExtra("url", tmp.getProfile_url());
                                context.startActivity(intent);
                            }


                        }

                    }
                });
            }
        });


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

        responseNotiRef = db.collection("user").document(requestUser.getUid())
                .collection("notification").document();

    }


    private void rejectRequest(final int position) {

        WriteBatch batch = db.batch();
        batch.delete(requestRef);
        batch.delete(responseRef);
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handler.post(new ListUpdateAction(position));
            }
        });


    }

    private void acceptRequest(final int position) {

        WriteBatch batch = db.batch();
        User responseUser = makeLoginUserInstance();


        Notification notification = new Notification();

        notification.setProfile_url(responseUser.getProfile_url());
        notification.setContent(responseUser.getName() + "님과 친구가 되었습니다.");
        notification.setNotiType(NotificationType.FRIEND_RESPONSE.name());
        notification.setDate(new Date());
        notification.setCheckNoti(false);


        batch.set(addFriendRes, new UploadUser(responseUser));
        batch.set(addFriendReq, new UploadUser(requestUser));
        batch.set(responseNotiRef, notification);

        batch.delete(requestRef);
        batch.delete(responseRef);


        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handler.post(new ListUpdateAction(position));
//                addFriend(AdapterTempStorage.getAdapter(), requestUser);

            }
        });

    }


    private User makeLoginUserInstance() {

        User meUser = new User();
        meUser.setUid(loginUser.getUid());
        meUser.setEmail(loginUser.getEmail());
        meUser.setName(loginUser.getDisplayName());
        meUser.setGender(null);
        if (loginUser.getPhotoUrl() != null)
            meUser.setProfile_url(loginUser.getPhotoUrl().toString());

        return meUser;
    }

//    private void addFriend(FriendAdapter adapter, User model) {
//        adapter.getArrayList().add(model);
//        adapter.notifyDataSetChanged();
//
//    }

    class AcceptRunnable implements Runnable {
        int position;

        AcceptRunnable(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            acceptRequest(position);
        }
    }

    class RejectRunnable implements Runnable {
        int position;

        RejectRunnable(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            rejectRequest(position);
        }
    }

    class ListUpdateAction implements Runnable {
        int position;

        ListUpdateAction(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            adapter.removeItem(position);
        }
    }


}
