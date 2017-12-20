package com.globe.hand.Main;

import android.os.Bundle;
import android.util.Log;

import com.globe.hand.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.enums.MenuPane;
import com.globe.hand.Main.fragments.KakaoUserProfileFragment;
import com.globe.hand.Main.fragments.MainMenuPaneFragment;
import com.globe.hand.Main.fragments.MapRoomFragment;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Map;

public class MainActivity extends BaseActivity
        implements MainMenuPaneFragment.OnMenuPaneClickListener,
                   MapRoomFragment.OnMapRoomInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(R.id.main_toolbar, false);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content_container,
                        MainMenuPaneFragment.newInstance())
                .commit();


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

    @Override
    public void OnMenuPaneClick(MenuPane menuPane) {
        switch (menuPane) {
            case MAP:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content_container,
                                MapRoomFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case FRIEND:
                break;
            case EVENT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapRoomInteraction(int roomId) {
        // TODO : MapRoom 중 하나를 눌렀을 경우
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
}
