package com.globe.hand.Main.Tab1Map.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.activities.controllers.adapters.MapPostViewPagerAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapPost;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ShowMapPostViewPagerFragment extends Fragment {
    private static final String MAP_ROOM_UID = "map_room_uid";

    private String mapRoomUid;

    private ViewPager viewPager;

    private ListenerRegistration registration;

    private List<MapPost> postList;

    public ShowMapPostViewPagerFragment() {
        // Required empty public constructor
    }

    public static ShowMapPostViewPagerFragment newInstance(String mapRoomUid) {
        ShowMapPostViewPagerFragment fragment = new ShowMapPostViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MAP_ROOM_UID, mapRoomUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_show_map_post, container, false);

        viewPager = view.findViewById(R.id.map_post_view_pager);
        viewPager.setPageMargin(50);
//        viewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -9);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        registration = db.collection("map_room").document(mapRoomUid)
                .collection("map_post").addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                postList = documentSnapshots.toObjects(MapPost.class);
                                viewPager.setAdapter(new MapPostViewPagerAdapter(
                                        getActivity(), postList));
                            }
                        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapRoomUid = getArguments().getString(MAP_ROOM_UID);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registration.remove();
    }
}
