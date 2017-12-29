package com.globe.hand.Main.Tab3Friend;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.globe.hand.FriendSearch.FriendSearchActivity;
import com.globe.hand.Main.Tab3Friend.fragment.FriendList;
import com.globe.hand.Main.Tab3Friend.fragment.RequestList;
import com.globe.hand.R;

public class MainFriendTabFragment extends Fragment {

    Button btnFriendList;
    Button btnRequestList;
    FloatingActionButton fab;
    int fragmentFlage;
    final int FRIEND_FRAGMENT = 0;
    final int REQUEST_FRAGMENT = 1;


    //friend_fragment_container
    public static MainFriendTabFragment newInstance() {
        return new MainFriendTabFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_main_friend_tab, container, false);
            btnFriendList = v.findViewById(R.id.friend_fragment_friend_btn);
            btnRequestList = v.findViewById(R.id.friend_fragment_request_btn);
            fab = v.findViewById(R.id.friend_fragment_fab);


            btnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFriendFragment();
            }
        });

        btnRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRequestFragment();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendSearchActivity.class);
                getContext().startActivity(intent);
            }
        });


        fragmentFlage = FRIEND_FRAGMENT;


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragment(FriendList.newInstance());
    }


    public void changeRequestFragment() {
        if (fragmentFlage != REQUEST_FRAGMENT) {
            fragmentFlage = REQUEST_FRAGMENT;
            replaceFragment(RequestList.newInstance());
        }
    }


    public void changeFriendFragment() {
        if (fragmentFlage != FRIEND_FRAGMENT) {
            fragmentFlage = FRIEND_FRAGMENT;
            replaceFragment(FriendList.newInstance());
        }
    }


    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.friend_fragment_container, fragment)
                .commit();
    }
}
