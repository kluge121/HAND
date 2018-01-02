package com.globe.hand.Main.Tab1Map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.controllers.adapters.MapRoomFirebaseRecyclerViewAdapter;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewEmptySupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class MainMapRoomFragment extends Fragment {

    private ListenerRegistration registration;

    public MainMapRoomFragment() {}

    public static MainMapRoomFragment newInstance() {
        return new MainMapRoomFragment();
    }

    private RecyclerViewEmptySupport recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_map_tab, container, false);

        recyclerView = v.findViewById(R.id.map_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setEmptyView(v.findViewById(R.id.map_empty_view));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // TODO : 스크롤하면 더 나오는거 해야함
        registration = db.collection("map_room").document(user.getUid())
            .collection("joined_map_rooms").orderBy("joinedDate").limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.e("map_room_snapshot", e.getMessage());
                    return;
                }
                recyclerView.setAdapter(new MapRoomFirebaseRecyclerViewAdapter(
                        getContext(), documentSnapshots.getDocuments()));
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registration != null) {
            registration.remove();
        }
    }
}
