package com.globe.hand.Main.Tab1Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.controllers.MapAdapter;
import com.globe.hand.Main.Tab1Map.controllers.RecyclerViewDecoration;
import com.globe.hand.R;


public class MainMapTabFragment extends Fragment {
    public static MainMapTabFragment newInstance() {
        return new MainMapTabFragment();
    }

    RecyclerView recyclerView;
    MapAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_map_tab, container, false);

        recyclerView = v.findViewById(R.id.map_recyclerview);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(10, 10));
        adapter = new MapAdapter(getContext());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }
}
