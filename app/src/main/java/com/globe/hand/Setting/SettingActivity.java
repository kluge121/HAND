package com.globe.hand.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.globe.hand.Main.fragments.FirebaseUserProfileFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar(R.id.setting_toolbar, true);
        setToolbarTitle("설정");


        getSupportFragmentManager().beginTransaction()
                .add(R.id.setting_profile_container, FirebaseUserProfileFragment.newInstance())
                .commit();

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
            findPreference("logout").setOnPreferenceClickListener(this);
            findPreference("sign_out").setOnPreferenceClickListener(this);
            findPreference("problem").setOnPreferenceClickListener(this);
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
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut();
                        if (Session.getCurrentSession().checkAndImplicitOpen()) {
                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    ((SettingActivity) getActivity()).redirectLoginActivity();
                                }
                            });
                        }
                        ((SettingActivity) getActivity()).redirectLoginActivity();
                    }
                    break;
                case "sign_out":
                    final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
                    new AlertDialog.Builder(getActivity())
                            .setMessage(appendMessage)
                            .setPositiveButton(getString(R.string.com_kakao_ok_button),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        db.collection("map_room").document(user.getUid())
                                                                .delete();
                                                        db.collection("user").document(user.getUid())
                                                                .delete();
                                                        if (task.isSuccessful()) {
                                                            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                                                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                                                    @Override
                                                                    public void onFailure(ErrorResult errorResult) {
                                                                        Logger.e(errorResult.toString());
                                                                    }

                                                                    @Override
                                                                    public void onSessionClosed(ErrorResult errorResult) {
                                                                        ((SettingActivity) getActivity())
                                                                                .redirectLoginActivity(getActivity(),
                                                                                        errorResult.toString());
                                                                    }

                                                                    @Override
                                                                    public void onNotSignedUp() {
                                                                        // ((SettingActivity) getActivity()).redirectMainActivity();
                                                                    }

                                                                    @Override
                                                                    public void onSuccess(Long userId) {
                                                                        ((SettingActivity) getActivity()).redirectLoginActivity();
                                                                    }
                                                                });
                                                            } else {
                                                                ((SettingActivity) getActivity()).redirectLoginActivity();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }).setNegativeButton(getString(R.string.com_kakao_cancel_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case "problem":
                    break;
            }
            return false;
        }
    }
}
