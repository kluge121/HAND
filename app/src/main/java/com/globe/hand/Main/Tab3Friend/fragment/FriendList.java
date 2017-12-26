package com.globe.hand.Main.Tab3Friend.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hand.R;


public class FriendList extends Fragment {


    RecyclerView recyclerView;
    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friend_list,container,false);


        recyclerView = v.findViewById(R.id.friend_recyclerview);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface OnCallbackFriendList {
        void changeFragment();
    }




}
