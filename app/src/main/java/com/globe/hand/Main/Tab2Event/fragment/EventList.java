package com.globe.hand.Main.Tab2Event.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.Main.Tab2Event.fragment.controllers.EventAdapter;
import com.globe.hand.R;
import com.globe.hand.common.RecyclerViewDecoration;

public class EventList extends Fragment {


    private RecyclerView recyclerView;
    private EventAdapter adapter;

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

        return v;
    }
}
