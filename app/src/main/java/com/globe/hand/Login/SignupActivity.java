package com.globe.hand.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.globe.hand.R;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        requestMe();
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
                Log.d("SignupActivity", "onFailure: " + message);
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

                TextView textView = findViewById(R.id.user_info);
                textView.setText(result.toString());
            }
        });
    }
}
