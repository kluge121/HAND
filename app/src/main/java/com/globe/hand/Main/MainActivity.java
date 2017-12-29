package com.globe.hand.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;
import com.globe.hand.Main.controllers.MainTabPagerAdapter;
import com.globe.hand.Setting.SettingActivity;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab1Map.MainMapRoomFragment;
import com.globe.hand.R;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(R.id.main_toolbar, false);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_setting);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,
                            SettingActivity.class));
                }
            });
        }
        setToolbarTitle("");

        ViewPager viewPager = findViewById(R.id.main_tab_container);
        setTabViewPager(viewPager);

        tabLayout = findViewById(R.id.main_tab);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_main_search) {
            //TODO: 검색
//            if(tabLayout.getSelectedTabPosition() == 0) {
//
//            }
        }
        return super.onOptionsItemSelected(item);
    }
}
