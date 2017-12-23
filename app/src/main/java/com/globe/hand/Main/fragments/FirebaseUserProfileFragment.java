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
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserProfileFragment extends Fragment {

    public FirebaseUserProfileFragment() {
        // Required empty public constructor
    }

    public static FirebaseUserProfileFragment newInstance() {
        return new FirebaseUserProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_user_profile, container, false);
        ImageView userImage = view.findViewById(R.id.image_user_profile);
        TextView userInfo = view.findViewById(R.id.text_user_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);
        }

        if(user.getDisplayName() != null) {
            userInfo.setText(String.format(
                    getString(R.string.user_profile_name_format), user.getDisplayName()));
        } else {
            userInfo.setText(getString(R.string.user_profile_name_empty));
        }

//        ImageView imageView = view.findViewById(R.id.image_my_event);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        ImageView imageView = view.findViewById(R.id.image_setting);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
        return view;
    }
}
