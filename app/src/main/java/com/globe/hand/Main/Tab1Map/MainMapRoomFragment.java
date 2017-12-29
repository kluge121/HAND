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

import com.globe.hand.Main.Tab1Map.controllers.adapters.MapRoomRecyclerViewAdapter;
import com.globe.hand.Main.fragments.FirebaseUserProfileFragment;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewEmptySupport;
import com.globe.hand.models.MapRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainMapRoomFragment extends Fragment {

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

//        replaceUserProfileFragment(FirebaseUserProfileFragment.newInstance());

        recyclerView = v.findViewById(R.id.map_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setEmptyView(v.findViewById(R.id.map_empty_view));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("map_room").document(user.getUid())
            .collection("joined_map_rooms").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.e("map_room_snapshot", e.getMessage());
                    return;
                }

                // TODO: 일단 임시로 이렇게 해놨으나 백퍼 수정이 필요함
                final ArrayList<MapRoom> mapRoomArrayList = new ArrayList<>();
                for(final DocumentSnapshot documentSnapshot: documentSnapshots.getDocuments()) {
                    final DocumentReference mapRoomReference =
                            (DocumentReference) documentSnapshot.get("mapRoomReference");
                    mapRoomReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().get("uid").equals(user.getUid())) {
                                    mapRoomArrayList.add(0, task.getResult().toObject(MapRoom.class));
                                } else {
                                    mapRoomArrayList.add(task.getResult().toObject(MapRoom.class));
                                }
                                updateRecyclerAdapter(mapRoomArrayList);
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    private void updateRecyclerAdapter(ArrayList<MapRoom> mapRoomArrayList) {
        recyclerView.setAdapter(new MapRoomRecyclerViewAdapter(
                getContext(), mapRoomArrayList));
    }

//    private void replaceUserProfileFragment(Fragment fragment) {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.user_profile_container,
//                        fragment)
//                .commit();
//    }
}
