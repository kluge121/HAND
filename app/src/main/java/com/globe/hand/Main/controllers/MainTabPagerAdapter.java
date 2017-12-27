package com.globe.hand.Main.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.globe.hand.Main.Tab1Map.MainMapTabFragment;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 27..
 */

public class MainTabPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mainListFragments = new ArrayList<Fragment>();

    private String tabTitle[] = new String[]{"지도", "이벤트", "친구", "알람"};

    public MainTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }


    @Override
    public Fragment getItem(int position) {
        return mainListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mainListFragments.size();
    }

    public void addFragment(MainMapTabFragment fragment1) {
        mainListFragments.add(fragment1);
    }

    public void addFragment(MainEventTabFragment fragment2) {
        mainListFragments.add(fragment2);
    }

    public void addFragment(MainFriendTabFragment fragment3) {
        mainListFragments.add(fragment3);
    }

    public void addFragment(MainAlarmTabFragment fragment4) {
        mainListFragments.add(fragment4);
    }

}
