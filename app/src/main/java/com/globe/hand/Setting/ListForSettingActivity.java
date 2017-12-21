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
import com.globe.hand.Setting.fragments.SettingRecyclerViewFragment;

public class ListForSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setToolbar(R.id.list_setting_toolbar, true);

        if(getIntent().getStringExtra("what").equals("notice")) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_recycler_view_container,
                            SettingRecyclerViewFragment.newInstance())
                    .commit();
        } else if(getIntent().getStringExtra("what").equals("question")){
            //recyclerView.setAdapter();
        }
    }
}
