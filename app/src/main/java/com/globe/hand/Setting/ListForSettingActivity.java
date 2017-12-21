package com.globe.hand.Setting;

import android.os.Bundle;

import com.globe.hand.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.Setting.fragments.SettingPostFragment;
import com.globe.hand.Setting.fragments.SettingRecyclerViewFragment;

public class ListForSettingActivity extends BaseActivity
        implements OnUpdateListForSettingFragmentListener {

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

    @Override
    public void updateFragment(String documentId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_recycler_view_container,
                        SettingPostFragment.newInstance(documentId))
                .addToBackStack(null)
                .commit();
    }
}
