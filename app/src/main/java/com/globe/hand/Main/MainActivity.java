package com.globe.hand.Main;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.globe.hand.BaseActivity;
import com.globe.hand.Main.adapters.MainTabFragmentAdpater;
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
    ViewPager viewPager;
    MainTabFragmentAdpater mainTabFragmentAdpater;
    MainMapTabFragment tabFragment1;
    MainEventTabFragment tabFragment2;
    MainFriendTabFragment tabFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setWidget();


        tabLayout.setupWithViewPager(viewPager);
        setTabViewPager(viewPager);



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
            public void onSuccess(final UserProfile result) {
                // TODO : 나중에 로딩같은 조치가 필요함
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_user_profile_container,
                                        KakaoUserProfileFragment.newInstance(result))
                                .commit();
                    }
                }, 500);
            }
        });
    }

    void setWidget() {
        tabLayout = findViewById(R.id.main_tab);
        viewPager = findViewById(R.id.main_tab_viewpager);

    }

    private void setTabViewPager(ViewPager viewPager) {
        mainTabFragmentAdpater = new MainTabFragmentAdpater(getSupportFragmentManager());
        tabFragment1 = MainMapTabFragment.newInstance();
        tabFragment2 = MainEventTabFragment.newInstance();
        tabFragment3 = MainFriendTabFragment.newInstance();

        mainTabFragmentAdpater.addFragment(tabFragment1);
        mainTabFragmentAdpater.addFragment(tabFragment2);
        mainTabFragmentAdpater.addFragment(tabFragment3);

        viewPager.setAdapter(mainTabFragmentAdpater);

    }
}
