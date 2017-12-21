package com.globe.hand.Main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.globe.hand.BaseActivity;
import com.globe.hand.Main.Tab2Event.MainEventTabFragment;
import com.globe.hand.Main.Tab3Friend.MainFriendTabFragment;
import com.globe.hand.Main.Tab1Map.MainMapTabFragment;
import com.globe.hand.R;
import com.globe.hand.Main.fragments.KakaoUserProfileFragment;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Map;

public class MainActivity extends BaseActivity {

    TabLayout tabLayout;


    final int MAP_TAB = 0;
    final int EVENT_TAB = 1;
    final int FRIEND_TAB = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.main_tab);
        tabLayout.addTab(tabLayout.newTab().setText("지도"));
        tabLayout.addTab(tabLayout.newTab().setText("이벤트"));
        tabLayout.addTab(tabLayout.newTab().setText("친"));


        setWidget();
        replaceFragment(MainMapTabFragment.newInstance());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                    case MAP_TAB:
                        replaceFragment(MainMapTabFragment.newInstance());
                        break;
                    case EVENT_TAB:
                        replaceFragment(MainEventTabFragment.newInstance());
                        break;
                    case FRIEND_TAB:
                        replaceFragment(MainFriendTabFragment.newInstance());
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


//        setToolbar(R.id.main_toolbar, false);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.main_content_container,
//                        MainMenuPaneFragment.newInstance())
//                .commit();

//        StringBuilder stringBuilder;
//                       = new StringBuilder("-- facebook user info --\n");
        if (!getIntent().getBooleanExtra("facebook", false)) {
            requestMe();
        }
//        Bundle userInfoBundle = getIntent().getBundleExtra("user_info");
//        for (String key : userInfoBundle.keySet()) {
//            Object value = userInfoBundle.get(key);
//            stringBuilder.append(String.format("%s %s (%s)\n", key,
//                    value.toString(), value.getClass().getName()));
//        }
//        TextView textView = findViewById(R.id.text_user_name);
//        textView.setText(stringBuilder.toString());
    }


    private void requestSignUp(final Map<String, String> properties) {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(Long result) {
                requestMe();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "User management ResponseCallback - failure : " + errorResult;
                com.kakao.util.helper.log.Logger.w(message);
//                KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
        }, properties);
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "유저 정보 받기 실패 == " + errorResult;
                Log.d("MainActivity", "onFailure: " + message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                // nothing
            }

            @Override
            public void onNotSignedUp() {
                // nothing
            }

            @Override
            public void onSuccess(UserProfile result) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_user_profile_container,
                                KakaoUserProfileFragment.newInstance(result))
                        .commit();
            }
        });
    }

    void setWidget() {

    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_tab_container, fragment);

        transaction.commit();
    }
}
