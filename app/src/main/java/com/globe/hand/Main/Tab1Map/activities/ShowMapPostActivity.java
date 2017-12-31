package com.globe.hand.Main.Tab1Map.activities;

import android.os.Bundle;

import com.globe.hand.R;
import com.globe.hand.common.BaseActivity;

public class ShowMapPostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map_post);

        setToolbar(R.id.show_map_post_toolbar, false);
        setToolbarTitle(getIntent().getStringExtra("map_room_uid"));
    }
}
