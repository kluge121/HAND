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

/*
 * Created by baeminsu on 2017. 12. 31.
 */

public class FriendSearchActivity2 extends BaseActivity {


    public final int ALREADY_REQUEST = 0;
    public final int ALREADY_FRIEND = 1;
    public final int NOMAL = 2;

    private FirebaseFirestore db;

    ArrayList<User> noFilterArrayList;
    ArrayList<User> requestList;
    ArrayList<User> friendList;
    ArrayList<CheckUser> checkUserList;

    SearchView searchView;
    RecyclerView recyclerView;
    FriendSearchAdapter adapter;
    AsyncGetSearchRequestList asyncGetList;
    AsyncSearchListCheck asyncCheck;

    boolean friendListNullChecker = false;
    boolean requestListNullChecker = false;

    boolean friendWait = false;
    boolean requestWait = false;


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

                asyncGetList = new AsyncGetSearchRequestList();
                asyncGetList.execute(s);

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

        if (asyncGetList.getStatus() == AsyncTask.Status.RUNNING)
            asyncGetList.cancel(true);

        if (asyncCheck.getStatus() == AsyncTask.Status.RUNNING)
            asyncCheck.cancel(true);

    }

    class AsyncSearchListCheck extends AsyncTask<ArrayList<User>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList... arrayLists) {


            User loginUser = GetLoginUserEntity.makeLoginUserInstance();

            CollectionReference myReqListRef = db.collection("user").document(loginUser.getUid())
                    .collection("reqeustFriend");
            CollectionReference myFriendListRef = db.collection("user").document(loginUser.getUid())
                    .collection("friend");

            myReqListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        requestList = (ArrayList<User>) task.getResult().toObjects(User.class);
                        if (!ObjectUtils.isEmpty(requestList) || requestList != null)
                            requestListNullChecker = true;
                    }
                    requestWait = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    requestWait = true;
                }
            });

            myFriendListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        friendList = (ArrayList<User>) task.getResult().toObjects(User.class);
                        if (!ObjectUtils.isEmpty(friendList) || friendList != null)
                            friendListNullChecker = true;
                    }
                    friendWait = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    friendWait = true;
                }
            });
            int count = 0;

//            while ((!requestWait && !friendWait)) {


            while ((!requestWait && !friendWait) || count < 10000) {
                Log.e("체크", count + "개");
                count++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int requestFlag = 0;
            int friendFlag = 0;
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

    class AsyncGetSearchRequestList extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String nameArg = strings[0];

            Query query = db.collection("user").whereEqualTo("name", nameArg);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    noFilterArrayList = (ArrayList<User>) documentSnapshots.toObjects(User.class);


                    checkUserList = new ArrayList<>();
                    asyncCheck = new AsyncSearchListCheck();
                    asyncCheck.execute(noFilterArrayList);


                }
            });

            return null;
        }
    }


}
