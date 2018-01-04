package com.globe.hand.Main.Tab3Friend.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.globe.hand.Main.Tab1Map.controllers.adapters.MapRoomFirebaseRecyclerViewAdapter;
import com.globe.hand.Main.Tab3Friend.fragment.controllers.FriendAdapter;
import com.globe.hand.Main.Tab3Friend.fragment.controllers.RequestViewHolder;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewDecoration;
import com.globe.hand.models.User;
import com.globe.hand.temp.AdapterTempStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendList extends Fragment {


    public static FriendList newInstance() {
        return new FriendList();
    }


    private RecyclerView recyclerView;
    private CircleImageView myUserProfileImage;
    private TextView myUserName;
    private TextView myUserEmail;
    private final String TAG = "FriendListActivity";
    private FriendAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser loginUser;
    private ListenerRegistration registration;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friend_list, container, false);

        recyclerView = v.findViewById(R.id.friend_recyclerview);


        AdapterTempStorage.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(0, 30));
        recyclerView.setNestedScrollingEnabled(false);

        db = FirebaseFirestore.getInstance();
        loginUser = FirebaseAuth.getInstance().getCurrentUser();

        myUserProfileImage = v.findViewById(R.id.my_user_profile);
        myUserName = v.findViewById(R.id.my_user_name);
        myUserEmail = v.findViewById(R.id.my_user_email);


        if (loginUser.getPhotoUrl() != null)
            Glide.with(getContext()).load(loginUser.getPhotoUrl()).into(myUserProfileImage);
        myUserName.setText(loginUser.getDisplayName());
        myUserEmail.setText(loginUser.getEmail());


        new AyncGetFriend().execute();

        return v;
    }


    private class AyncGetFriend extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            registration =
                    db.collection("user").document(loginUser.getUid()).
                            collection("friend").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("map_room_snapshot", e.getMessage());
                                return;
                            }
                            recyclerView.setAdapter(new FriendAdapter(
                                    getContext(), documentSnapshots.getDocuments()));

                        }
                    });



            return null;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}
