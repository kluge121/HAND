package com.globe.hand.Main.Tab1Map.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.MapPostFirebaseViewPagerAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.globe.hand.models.MapPostReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ShowMapPostViewPagerFragment extends Fragment {
    private static final String MAP_ROOM_UID = "map_room_uid";
    private static final String CURRENT_MAP_POST_UID = "current_map_post_uid";

    private String mapRoomUid;
    private String currentMapPostUid;

    private ViewPager viewPager;

    public ShowMapPostViewPagerFragment() {
        // Required empty public constructor
    }

    public static ShowMapPostViewPagerFragment newInstance(String mapRoomUid,
                                                           String currentMapPostUid) {
        ShowMapPostViewPagerFragment fragment = new ShowMapPostViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MAP_ROOM_UID, mapRoomUid);
        bundle.putString(CURRENT_MAP_POST_UID, currentMapPostUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(
                R.layout.fragment_show_map_post, container, false);

        viewPager = view.findViewById(R.id.map_post_view_pager);
        viewPager.setPageMargin(50);
//        viewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -9);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("map_room").document(mapRoomUid)
                .collection("map_post_ref").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<MapPostReference> mapPostReferenceList
                                    = task.getResult().toObjects(MapPostReference.class);

                            viewPager.setAdapter(new MapPostFirebaseViewPagerAdapter(
                                    getActivity(), mapPostReferenceList));

                            DocumentReference currentMapPostReference
                                    = db.collection("map_room").document(mapRoomUid)
                                    .collection("map_post_ref").document(currentMapPostUid);
                            int currentIndex = 0;
                            for (MapPostReference mapPostReference : mapPostReferenceList) {
                                if (mapPostReference.getMapPostReference().equals(currentMapPostReference)) {
                                    currentIndex = mapPostReferenceList.indexOf(mapPostReference);
                                    break;
                                }
                            }
                            viewPager.setCurrentItem(currentIndex);

                        }
                    }
                });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapRoomUid = getArguments().getString(MAP_ROOM_UID);
            currentMapPostUid = getArguments().getString(CURRENT_MAP_POST_UID);
        }
    }
}
