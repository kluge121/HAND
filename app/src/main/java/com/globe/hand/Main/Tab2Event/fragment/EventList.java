package com.globe.hand.Main.Tab2Event.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.globe.hand.Main.Tab2Event.fragment.controllers.EventAdapter;
import com.globe.hand.Main.Tab2Event.models.EventEntity;
import com.globe.hand.Main.Tab2Event.models.EventRefEntity;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class EventList extends Fragment {


    private RecyclerView recyclerView;
    private EventAdapter adapter;
    ArrayList<EventEntity> eventList;
    ArrayList<EventRefEntity> eventRefList;
    ArrayList<EventEntity> checkEventList;
    CollectionReference userEventRef;
    CollectionReference evnetRef;

    public static EventList newInstance() {
        return new EventList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerView = v.findViewById(R.id.event_tab_recyclerview);
        adapter = new EventAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(0, 20));

        getEventList();

        return v;
    }


    void getEventList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

        evnetRef = db.collection("event");

        userEventRef = db.collection("user").document(loginUser.getUid())
                .collection("userEvent");


        evnetRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                    }

                    String source = documentSnapshots.getMetadata().isFromCache() ?
                            "local cache" : "server";
                }

            }
        });


        evnetRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                    }

                    String source = documentSnapshots.getMetadata().isFromCache() ?
                            "local cache" : "server";
                }


            }
        });


        evnetRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    eventList = (ArrayList<EventEntity>) task.getResult().toObjects(EventEntity.class);

                    userEventRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                checkEventList = new ArrayList<>();
                                eventRefList = (ArrayList<EventRefEntity>) task.getResult().toObjects(EventRefEntity.class);

                                int flag = 0;
                                for (int i = 0; i < eventList.size(); i++) {

                                    Log.e("체크", eventList.get(i).getContent());
                                    flag = 0;

                                    for (int j = 0; j < eventRefList.size(); j++) {

                                        if (eventList.get(i).getUid().equals(eventRefList.get(j).getUid())) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag != 1)
                                        checkEventList.add(eventList.get(i));

                                }

                                adapter.setArrayList(checkEventList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }
}
