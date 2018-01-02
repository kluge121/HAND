package com.globe.hand.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;


import com.globe.hand.FriendSearch.FriendSearchActivity2;
import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;
import com.globe.hand.Main.controllers.MainTabPagerAdapter;
import com.globe.hand.Setting.SettingActivity;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab1Map.MainMapRoomFragment;
import com.globe.hand.R;
import com.globe.hand.common.SearchViewStyle;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private ImageButton settingBtn;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.main_searchview);

        SearchViewStyle.on(searchView)
                .setCursorColor(Color.BLACK)
                .setSearchButtonImageResource(R.drawable.search_non_click_icon)
                .setTextColor(Color.BLACK);


        settingBtn = findViewById(R.id.main_setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });



        ViewPager viewPager = findViewById(R.id.main_tab_container);
        setTabViewPager(viewPager);

        tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.tab_ic_map_room);
        tabLayout.getTabAt(1).setIcon(R.drawable.tab_ic_friend);
        tabLayout.getTabAt(2).setIcon(R.drawable.tab_ic_event);
        tabLayout.getTabAt(3).setIcon(R.drawable.tab_ic_notification);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1)
                    fab.setVisibility(View.INVISIBLE);
                else
                    fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab = findViewById(R.id.friend_fragment_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendSearchActivity2.class);
                startActivity(intent);
            }
        });


    }

    private void setTabViewPager(ViewPager viewPager) {
        MainTabPagerAdapter mainTabPagerAdapter =
                new MainTabPagerAdapter(getSupportFragmentManager());

        mainTabPagerAdapter.addFragment(MainMapRoomFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainFriendTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainEventTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainAlarmTabFragment.newInstance());
        viewPager.setAdapter(mainTabPagerAdapter);
    }



}
