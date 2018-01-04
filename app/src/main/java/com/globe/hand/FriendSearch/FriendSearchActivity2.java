package com.globe.hand.FriendSearch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.globe.hand.FriendSearch.controllers.FriendSearchAdapter;
import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.common.GetLoginUserEntity;
import com.globe.hand.common.ObjectUtils;
import com.globe.hand.common.RecyclerViewDecoration;
import com.globe.hand.models.CheckUser;
import com.globe.hand.models.UploadUser;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created by baeminsu on 2017. 12. 31.
 */

public class FriendSearchActivity2 extends BaseActivity {


    public final int ALREADY_REQUEST = 0;
    public final int ALREADY_FRIEND = 1;
    public final int NOMAL = 2;

    private FirebaseFirestore db;

    List<User> noFilterArrayList;
    List<UploadUser> requestList;
    List<UploadUser> friendList;
    ArrayList<CheckUser> checkUserList;

    SearchView searchView;
    RecyclerView recyclerView;
    FriendSearchAdapter adapter;

    AsyncGetFriendList asyncGetFriendList;
//    AsyncGetSearchRequestList asyncGetList;
//    AsyncSearchListCheck asyncCheck;


    boolean friendListNullChecker = false;
    boolean requestListNullChecker = false;

    boolean friendWait = false;
    boolean requestWait = false;

    User loginUser = GetLoginUserEntity.makeLoginUserInstance();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        recyclerView = findViewById(R.id.friend_search_recyclerview);
        adapter = new FriendSearchAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(0, 30));


        getMenuInflater().inflate(R.menu.menu_new_friend_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                asyncGetFriendList = new AsyncGetFriendList();
                asyncGetFriendList.execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search2);
        setToolbar(R.id.friend_search_toolbar, true);

        db = FirebaseFirestore.getInstance();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    class AsyncGetFriendList extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            db.collection("user").whereEqualTo("name", strings[0])
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        noFilterArrayList = task.getResult().toObjects(User.class);


                        CollectionReference myReqListRef = db.collection("user").document(loginUser.getUid())
                                .collection("reqeustFriend");

                        CollectionReference myFriendListRef = db.collection("user").document(loginUser.getUid())
                                .collection("friend");


                        myReqListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    requestList = task.getResult().toObjects(UploadUser.class);
                                    requestListNullChecker = true;
                                }
                                requestWait = true;
                            }
                        });


                        myFriendListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    friendList = task.getResult().toObjects(UploadUser.class);
                                    friendListNullChecker = true;
                                }

                                friendWait = true;
                            }
                        });


                    }


                }
            });
            int count = 0;
            while ((!requestWait && !friendWait) || count < 20000) {
                Log.e("체크", count + "번");
                count++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int requestFlag = 0;
            int friendFlag = 0;

            checkUserList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(noFilterArrayList)) {

                for (int i = 0; i < noFilterArrayList.size(); i++) {


                    requestFlag = 0;
                    friendFlag = 0;

                    if (requestListNullChecker) {
                        for (int j = 0; j < requestList.size(); j++) {
                            if (noFilterArrayList.get(i).getUid().equals(requestList.get(j).getUid())) {
                                CheckUser tmpUser = CheckUser.transUserToCheckUser(noFilterArrayList.get(i), ALREADY_REQUEST);
                                checkUserList.add(tmpUser);
                                requestFlag = 1;
                                break;
                            }
                        }
                    }
                    if (requestFlag != 1 && friendListNullChecker) {
                        for (int k = 0; k < friendList.size(); k++) {
                            if (noFilterArrayList.get(i).getUid().equals(friendList.get(k).getUid())) {
                                CheckUser tmpUser = CheckUser.transUserToCheckUser(noFilterArrayList.get(i), ALREADY_FRIEND);
                                checkUserList.add(tmpUser);
                                friendFlag = 1;
                                break;
                            }
                        }
                    }
                    if (requestFlag == 0 && friendFlag == 0 && !noFilterArrayList.get(i).getUid().equals(GetLoginUserEntity.makeLoginUserInstance().getUid())) {
                        CheckUser tmpUser = CheckUser.transUserToCheckUser(noFilterArrayList.get(i), NOMAL);
                        checkUserList.add(tmpUser);
                    }
                }
            }
            adapter.setArrayList(checkUserList);
            adapter.notifyDataSetChanged();
        }


    }

}



