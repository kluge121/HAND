package com.globe.hand.Main;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.globe.hand.Login.fragments.LoadingFragment;
import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;
import com.globe.hand.Main.controllers.MainTabPagerAdapter;
import com.globe.hand.Main.fragments.FirebaseUserProfileFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab1Map.MainMapTabFragment;
import com.globe.hand.R;
import com.globe.hand.Main.fragments.KakaoUserProfileFragment;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

public class MainActivity extends BaseActivity {



    TabLayout tabLayout;
    ViewPager viewPager;
    MainTabPagerAdapter mainTabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.main_tab);


        viewPager = findViewById(R.id.main_tab_container);
        setTabViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        replaceUserProfileFragment(FirebaseUserProfileFragment.newInstance());
    }

    private void replaceUserProfileFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_user_profile_container,
                        fragment)
                .commit();
    }

    private void replaceTabLayoutFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_tab_container, fragment)
                .commit();
    }


    private void setTabViewPager(ViewPager viewPager) {
        mainTabPagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager());

        mainTabPagerAdapter.addFragment(MainMapTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainEventTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainFriendTabFragment.newInstance());
        mainTabPagerAdapter.addFragment(MainAlarmTabFragment.newInstance());
        viewPager.setAdapter(mainTabPagerAdapter);

    }

}
