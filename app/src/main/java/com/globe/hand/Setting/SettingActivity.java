package com.globe.hand.Setting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.Preference;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.globe.hand.BaseActivity;
import com.globe.hand.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar(R.id.setting_toolbar, true);

        getFragmentManager().beginTransaction()
                .add(R.id.setting_container, SettingFragment.newInstance())
                .commit();
    }

    public static class SettingFragment extends PreferenceFragment
                implements Preference.OnPreferenceClickListener {
        public SettingFragment() {
            // 기본 퍼블릭 생성자가 필요함
        }

        public static SettingFragment newInstance() {
            return new SettingFragment();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            findPreference("notice").setOnPreferenceClickListener(this);
            findPreference("question").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "notice":
                    Intent noticeIntent =
                            new Intent(getActivity(), ListForSettingActivity.class);
                    noticeIntent.putExtra("what", "notice");
                    startActivity(noticeIntent);
                    break;
                case "question":
                    Intent questionIntent =
                            new Intent(getActivity(), ListForSettingActivity.class);
                    questionIntent.putExtra("what", "question");
                    startActivity(questionIntent);
                    break;
                case "logout":
                    break;
                case "sign_out":
                    break;
                case "problem":
                    break;
            }
            return false;
        }
    }
}
