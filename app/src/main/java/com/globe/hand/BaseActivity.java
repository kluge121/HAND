package com.globe.hand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.globe.hand.Login.LoginActivity;
import com.globe.hand.Main.MainActivity;

/**
 * Created by ssangwoo on 2017-12-18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
