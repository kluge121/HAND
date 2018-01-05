package com.globe.hand.Main.Tab4Alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.controllers.adapters.MapRoomFirebaseRecyclerViewAdapter;
import com.globe.hand.Main.Tab4Alarm.controllers.AlarmAdapter;
import com.globe.hand.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


public class MainAlarmTabFragment extends Fragment {

    private ListenerRegistration registration;
    RecyclerView recyclerView;
    AlarmAdapter adapter;

    public static MainAlarmTabFragment newInstance() {
        return new MainAlarmTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_alarm_tab, container, false);

        recyclerView = v.findViewById(R.id.alarm_tab_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        registration = db.collection("user").document(user.getUid())
                .collection("notification").orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("헤헤", e.getMessage());
                            return;
                        }
                        recyclerView.setAdapter(new AlarmAdapter(
                                getContext(), documentSnapshots.getDocuments()));
                    }
                });


        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }


}
