package com.globe.hand.Main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.R;
import com.globe.hand.Setting.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.usermgmt.response.model.UserProfile;

public class KakaoUserProfileFragment extends Fragment {
    private static final String USER_PROFILE_KEY = "user_profile";
    private UserProfile userProfile;

    public KakaoUserProfileFragment() {
        // Required empty public constructor
    }

    public static KakaoUserProfileFragment newInstance(UserProfile userProfile) {
        KakaoUserProfileFragment fragment = new KakaoUserProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_PROFILE_KEY, userProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userProfile = getArguments().getParcelable(USER_PROFILE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_user_profile, container, false);

        ImageView userImage = view.findViewById(R.id.image_user_profile);
        Glide.with(this)
                .load(userProfile.getProfileImagePath())
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);

        TextView userInfo = view.findViewById(R.id.text_user_name);
        userInfo.setText(userProfile.getNickname());

//        ImageView myEventImage = view.findViewById(R.id.image_my_event);
//        myEventImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        ImageView settingImage = view.findViewById(R.id.image_setting);
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
        return view;
    }
}
