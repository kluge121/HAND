package com.globe.hand.Main.Tab2Event;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.globe.hand.Main.Tab2Event.fragment.EventList;
import com.globe.hand.Main.Tab2Event.fragment.MyEventList;
import com.globe.hand.Main.Tab3Friend.fragment.FriendList;
import com.globe.hand.R;


public class MainEventTabFragment extends Fragment {


    int fragmentFlage;
    ImageButton btnEventList;
    ImageButton btnMyEventList;

    final int EVENT_FRAGMENT = 0;
    final int MY_EVENT_FRAGMENT = 1;


    public static MainEventTabFragment newInstance() {
        return new MainEventTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_event_tab, container, false);
        fragmentFlage = EVENT_FRAGMENT;
        replaceFragment(EventList.newInstance());

        btnEventList = v.findViewById(R.id.evnet_fragment_eventing_btn);
        btnEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentFlage != EVENT_FRAGMENT) {
                    replaceFragment(EventList.newInstance());
                    fragmentFlage = EVENT_FRAGMENT;
                }

            }
        });

        btnMyEventList = v.findViewById(R.id.evnet_fragment_my_event_btn);
        btnMyEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentFlage != MY_EVENT_FRAGMENT) {
                    replaceFragment(MyEventList.newInstance());
                    fragmentFlage = MY_EVENT_FRAGMENT;
                }

            }
        });


        return v;
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.evnet_fragment_container, fragment)
                .commit();
    }

    public void changeEventList() {
        if (fragmentFlage != EVENT_FRAGMENT) {
            fragmentFlage = EVENT_FRAGMENT;
            replaceFragment(EventList.newInstance());
        }
    }


    public void changeMyEventList() {
        if (fragmentFlage != MY_EVENT_FRAGMENT) {
            fragmentFlage = MY_EVENT_FRAGMENT;
            replaceFragment(FriendList.newInstance());
        }
    }


}
