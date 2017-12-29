package com.globe.hand.Main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;
import com.globe.hand.Main.controllers.MainTabPagerAdapter;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab1Map.MainMapRoomFragment;
import com.globe.hand.R;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.main_tab_container);
        setTabViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTabViewPager(ViewPager viewPager) {
        MainTabPagerAdapter mainTabPagerAdapter =
                new MainTabPagerAdapter(getSupportFragmentManager());

        mainTabPagerAdapter.addFragment(MainMapRoomFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainEventTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainFriendTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainAlarmTabFragment.newInstance());
        viewPager.setAdapter(mainTabPagerAdapter);
    }
}
