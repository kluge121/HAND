package com.globe.hand.Main.Tab3Friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab1Map.controllers.RecyclerViewDecoration;
import com.globe.hand.Main.Tab3Friend.controllers.FriendAdapter;
import com.globe.hand.R;


public class MainFriendTabFragment extends Fragment {
    public static MainFriendTabFragment newInstance() {
        return new MainFriendTabFragment();
    }

    RecyclerView recyclerView;
    SearchView searchView;
    FriendAdapter adapter;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_friend_tab, container, false);
        fab = v.findViewById(R.id.friend_fab);
        searchView = v.findViewById(R.id.friend_search_view);
        recyclerView = v.findViewById(R.id.friend_recyclerview);
        adapter = new FriendAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerViewDecoration(5, 0));

        return v;
    }
}
