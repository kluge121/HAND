package com.globe.hand;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.globe.hand.Login.LoginActivity;
import com.globe.hand.Main.MainActivity;

/**
 * Created by ssangwoo on 2017-12-18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;

    protected void setToolbar(int resId, boolean isSetIndicator) {
        toolbar = findViewById(resId);
        setSupportActionBar(toolbar);

        if(isSetIndicator) {
            setHomeAsUpIndicator();
        }
    }

    private void setHomeAsUpIndicator() {
        if(toolbar == null) return;
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

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
