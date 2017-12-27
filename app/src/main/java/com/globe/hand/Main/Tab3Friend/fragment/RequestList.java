package com.globe.hand.Main.Tab3Friend.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.globe.hand.Main.Tab3Friend.fragment.controllers.RequestAdapter;
import com.globe.hand.R;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RequestList extends Fragment {

    RecyclerView recyclerView;
    RequestAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseUser loginUser;

    public static RequestList newInstance() {
        return new RequestList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_request_list, container, false);
        adapter = new RequestAdapter(getContext());
        recyclerView = v.findViewById(R.id.firend_request_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loginUser = FirebaseAuth.getInstance().getCurrentUser();

        new AyncGetResponseList().execute();



        return v;
    }

    private class AyncGetResponseList extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.e("요청리스트", "진입");


            CollectionReference friendRef =
                    db.collection("user").document(loginUser.getUid()).
                            collection("responseFriend");

            Log.e("요청리스트 진입" , loginUser.getUid());

            friendRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        ArrayList<User> arrayList;
                        arrayList = (ArrayList<User>) task.getResult().toObjects(User.class);
                        adapter.setArrayList(arrayList);
                        adapter.notifyDataSetChanged();
                        Log.e("요청리스트", "성공");
                    } else {
                        Log.e("요청리스트", "실패");

                    }

                }
            });


            return null;
        }


    }


}
