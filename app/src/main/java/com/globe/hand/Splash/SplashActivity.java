package com.globe.hand.Splash;

import android.os.Bundle;
import android.os.Handler;

import com.globe.hand.common.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    redirectMainActivity();
                } else {
                    redirectLoginActivity();
                }
            }
        }, 1000);
    }
}
