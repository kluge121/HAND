package com.globe.hand.MapRoom.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.MapRoom.controllers.adapters.MapRoomRecyclerViewAdapter;
import com.globe.hand.R;
import com.globe.hand.models.MapRoom;

import java.util.ArrayList;

public class MapRoomFragment extends Fragment {

    public MapRoomFragment() {
        // Required empty public constructor
    }

    public static MapRoomFragment newInstance() {
        return new MapRoomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_map_room, container, false);
        RecyclerView mapRoomRecycler = view.findViewById(R.id.map_room_recycler_view);
        mapRoomRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // dummy
        ArrayList<MapRoom> mapRoomArrayList = new ArrayList<>();
        mapRoomArrayList.add(new MapRoom(0, R.drawable.talk, "테스트1"));
        mapRoomArrayList.add(new MapRoom(1, R.drawable.talk, "테스트2"));

        mapRoomRecycler.setAdapter(
                new MapRoomRecyclerViewAdapter(getContext(), mapRoomArrayList));

        return view;
    }


}
