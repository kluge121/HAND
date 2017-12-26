package com.globe.hand.Main.Tab3Friend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.globe.hand.R;

public class MainFriendTabFragment extends Fragment {

    Button btnFriendList;
    Button btnRequestList;


    //friend_fragment_container
    public static MainFriendTabFragment newInstance() {
        return new MainFriendTabFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_friend_list, container, false);
        btnFriendList = v.findViewById(R.id.friend_fragment_friend_btn);
        btnRequestList = v.findViewById(R.id.friend_fragment_request_btn);




        return v;
    }



}
