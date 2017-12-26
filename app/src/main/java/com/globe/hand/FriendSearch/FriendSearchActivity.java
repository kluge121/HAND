package com.globe.hand.FriendSearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Iterator;

public class FriendSearchActivity extends BaseActivity implements View.OnClickListener {


    final int SUCCESS = 0;
    final int ALREADY_SUCCESS = 1;

    User searchUser;

    SearchView searchView;
    ImageView ivProfile;
    TextView tvName;
    TextView tvNoti;
    Button btnAdd;
    Button btnCancel;

    private FirebaseFirestore db;
    private DocumentReference requestRef;
    private DocumentReference responseRef;

    FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        setToolbar(R.id.friend_search_toolbar, true);
        init();

    }

    void init() {
        searchView = findViewById(R.id.friend_search_searchview);
        ivProfile = findViewById(R.id.friend_search_profile);
        tvName = findViewById(R.id.friend_search_name);
        btnAdd = findViewById(R.id.friend_search_add);
        btnCancel = findViewById(R.id.friend_search_cancel);
        tvNoti = findViewById(R.id.friend_search_notify);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                runOnUiThread(new searchRunnable(query));
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    void searchSuccessWidget(User user, int state) {
        ivProfile.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvNoti.setVisibility(View.INVISIBLE);
        tvName.setText(user.getName());

        if (user.getProfile_url() != null) {
            Glide.with(this)
                    .load(user.getProfile_url())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }

        if (state == SUCCESS) {
            btnAdd.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
        } else if (state == ALREADY_SUCCESS) {
            btnCancel.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
        }

    }

    void searchFailWidget(String notiMessage) {
        ivProfile.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        btnAdd.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        tvNoti.setVisibility(View.VISIBLE);

        tvNoti.setText(notiMessage);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.friend_search_add:
                if (searchUser != null) {
                    runOnUiThread(new addRunnable());
                }
                break;

            case R.id.friend_search_cancel:
                if (searchUser != null) {
                    runOnUiThread(new cancelRunaable());
                }
                break;
        }

    }


    void searchFriend(String email) {

        //자기자신 검색 예외처리
        if (email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            searchFailWidget(getResources().getString(R.string.friend_search_me));
            return;
        }

        db = FirebaseFirestore.getInstance();

        CollectionReference userRef = db.collection("user");
        Query query = userRef.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().toObjects(User.class).size() != 0) {
                    searchUser = (User) task.getResult().toObjects(User.class).get(0);


                    //추가하려는 친구가 이미 등록된 친구인지 체크
                    CollectionReference friendDuplicationCheckRef = db
                            .collection("user").document(searchUser.getUid())
                            .collection("friend");

                    friendDuplicationCheckRef.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    Iterator<User> iterator = task.getResult().toObjects(User.class).iterator();
                                    User currentUser;
                                    //이미 등록된 친구 체크
                                    while (iterator.hasNext()) {
                                        currentUser = iterator.next();
                                        if (loginUser.getEmail().equals(currentUser.getEmail())) {
                                            searchFailWidget(getResources().getString(R.string.friend_search_friend));
                                            return;
                                        }
                                    }
                                }
                            });

                    CollectionReference friendAlreadyReqestCheckRef = db
                            .collection("user").document(searchUser.getUid())
                            .collection("responseFriend");

                    friendAlreadyReqestCheckRef.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Iterator<User> iterator = task.getResult().toObjects(User.class).iterator();
                                    User currentUser;

                                    while (iterator.hasNext()) {
                                        currentUser = iterator.next();
                                        if (loginUser.getEmail().equals(currentUser.getEmail())) {
                                            searchSuccessWidget(searchUser, ALREADY_SUCCESS);
                                            return;
                                        }
                                    }
                                }
                            });


                    //친구 신청을 이미 한 상태인지 체크


                    searchSuccessWidget(searchUser, SUCCESS);


                } else {
                    searchFailWidget(getResources().getString(R.string.friend_search_fail));
                }

            }

        });
    }

    void addFriend() {

        //친구 요청 트랜잭션
        WriteBatch batch = db.batch();

        User user = makeLoginUserInstance();
        friendRefSetting();

        batch.set(requestRef, searchUser);
        batch.set(responseRef, user);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("친구추가 성공", "친추성공");
                    searchSuccessWidget(searchUser, ALREADY_SUCCESS);
                } else {
                    Log.e("친구추가 실패", "친추실패");
                }
            }
        });
    }

    void cancelFriend() {

        WriteBatch batch = db.batch();

        friendRefSetting();

        batch.delete(requestRef);
        batch.delete(responseRef);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("친구삭제 성공", "삭제성공");
                    searchSuccessWidget(searchUser, SUCCESS);
                } else {
                    Log.e("친구삭제 실패", "삭제실패");
                }
            }
        });

    }


    //친구 검색
    class searchRunnable implements Runnable {
        String email;

        searchRunnable(String email) {
            this.email = email;
        }

        @Override
        public void run() {
            searchFriend(email);
        }
    }

    //친구 추가 요청
    class addRunnable implements Runnable {

        @Override
        public void run() {
            addFriend();
        }
    }

    //친구 요청 취소
    class cancelRunaable implements Runnable {
        @Override
        public void run() {
            cancelFriend();

        }
    }


    User makeLoginUserInstance() {
        User meUser = new User();
        meUser.setUid(loginUser.getUid());
        meUser.setEmail(loginUser.getEmail());
        meUser.setName(loginUser.getDisplayName());
        if (loginUser.getPhotoUrl() != null)
            meUser.setProfile_url(loginUser.getPhotoUrl());

        return meUser;
    }

    void friendRefSetting() {

        requestRef = db.collection("user").document(loginUser.getUid()).
                collection("reqeustFriend").document(searchUser.getUid());

        responseRef = db.collection("user").document(searchUser.getUid()).
                collection("responseFriend").document(loginUser.getUid());

    }


}
