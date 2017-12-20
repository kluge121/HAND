package com.globe.hand.Setting;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.globe.hand.BaseActivity;
import com.globe.hand.R;

public class ListForSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setToolbar(R.id.list_setting_toolbar, true);

        RecyclerView recyclerView = findViewById(R.id.setting_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().getStringExtra("what").equals("notice")) {
            //recyclerView.setAdapter();
        } else {
            //recyclerView.setAdapter();
        }
    }
}
