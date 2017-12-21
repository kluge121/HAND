package com.globe.hand.Main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.globe.hand.Main.fragments.MainEventTabFragment;
import com.globe.hand.Main.fragments.MainFriendTabFragment;
import com.globe.hand.Main.fragments.MainMapTabFragment;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2017. 12. 21..
 */


public class MainTabFragmentAdpater extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mainListFragment = new ArrayList<Fragment>();
    private String tabTitle[] = new String[]{"지도","이벤트","친구"};


    public MainTabFragmentAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mainListFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }


    @Override
    public int getCount() {
        return mainListFragment.size();
    }

    public void addFragment(MainMapTabFragment fragment1) {
        mainListFragment.add(fragment1);
    }

    public void addFragment(MainEventTabFragment fragment2) {
        mainListFragment.add(fragment2);
    }

    public void addFragment(MainFriendTabFragment fragment3) {
        mainListFragment.add(fragment3);
    }

}
