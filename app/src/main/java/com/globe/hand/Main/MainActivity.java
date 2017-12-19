package com.globe.hand.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.R;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);

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
        TextView textView = findViewById(R.id.user_info);
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
                ImageView userImage = findViewById(R.id.user_image);
                Glide.with(MainActivity.this)
                        .load(result.getProfileImagePath())
                        .apply(RequestOptions.circleCropTransform())
                        .into(userImage);

                TextView userInfo = findViewById(R.id.user_info);
                userInfo.setText(result.getNickname());
            }
        });
    }
}
