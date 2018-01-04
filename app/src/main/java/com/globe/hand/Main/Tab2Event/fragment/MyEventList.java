package com.globe.hand.Main.Tab2Event.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab2Event.fragment.controllers.EventAdapter;
import com.globe.hand.Main.Tab2Event.fragment.controllers.MyEventAdapter;
import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.Main.Tab2Event.models.EventRefEntity;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyEventList extends Fragment {


    RecyclerView recyclerView;
    MyEventAdapter adapter;

    public static MyEventList newInstance() {
        return new MyEventList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_event_list, container, false);

        recyclerView = v.findViewById(R.id.my_event_tab_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(0, 20));

        getMyEventList();


        return v;
    }

    void getMyEventList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference userEvnetRef = db.collection("user").document(loginUser.getUid())
                .collection("userEvent");


        userEvnetRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    adapter = new MyEventAdapter(task.getResult().getDocuments(), getContext());
                    recyclerView.setAdapter(adapter);


                }

            }
        });



    }

}
