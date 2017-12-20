package com.globe.hand.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.Setting.SettingActivity;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Map;

public class MainActivity extends BaseActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(R.id.main_toolbar, false);

        Button buttonSetting = findViewById(R.id.button_setting);
        buttonSetting.setOnClickListener(this);

        StringBuilder stringBuilder;
        if(getIntent().getBooleanExtra("facebook", false)) {
            stringBuilder = new StringBuilder("-- facebook user info --\n");
        } else {
            requestMe();
            return;
//            stringBuilder = new StringBuilder("-- kakao user info --\n");
        }
        Bundle userInfoBundle = getIntent().getBundleExtra("user_info");
        for (String key : userInfoBundle.keySet()) {
            Object value = userInfoBundle.get(key);
            stringBuilder.append(String.format("%s %s (%s)\n", key,
                    value.toString(), value.getClass().getName()));
        }
        TextView textView = findViewById(R.id.text_user_name);
        textView.setText(stringBuilder.toString());
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
                ImageView userImage = findViewById(R.id.image_user_profile);
                Glide.with(MainActivity.this)
                        .load(result.getProfileImagePath())
                        .apply(RequestOptions.circleCropTransform())
                        .into(userImage);

                TextView userInfo = findViewById(R.id.text_user_name);
                userInfo.setText(result.getNickname());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(view.getId() == R.id.button_setting) {
            intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
    }
}
