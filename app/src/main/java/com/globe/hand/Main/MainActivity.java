package com.globe.hand.Main;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.globe.hand.Login.fragments.LoadingFragment;
import com.globe.hand.Main.Tab4Alarm.MainAlarmTabFragment;
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

    private static final int MAP_TAB = 0;
    private static final int EVENT_TAB = 1;
    private static final int FRIEND_TAB = 2;
    private static final int ALARM_TAB = 3;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.main_tab);
        tabLayout.addTab(tabLayout.newTab().setText("지도"));
        tabLayout.addTab(tabLayout.newTab().setText("이벤트"));
        tabLayout.addTab(tabLayout.newTab().setText("친구"));
        tabLayout.addTab(tabLayout.newTab().setText("알람"));

        replaceTabLayoutFragment(LoadingFragment.newInstance());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case MAP_TAB:
                        replaceTabLayoutFragment(MainMapTabFragment.newInstance());
                        break;
                    case EVENT_TAB:
                        replaceTabLayoutFragment(MainEventTabFragment.newInstance());
                        break;
                    case FRIEND_TAB:
                        replaceTabLayoutFragment(MainFriendTabFragment.newInstance());
                        break;
                    case ALARM_TAB:
                        replaceTabLayoutFragment(MainAlarmTabFragment.newInstance());
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        replaceTabLayoutFragment(MainMapTabFragment.newInstance());
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
}
