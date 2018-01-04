package com.globe.hand.Main.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globe.hand.R;
import com.globe.hand.Setting.SettingActivity;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseUserProfileFragment extends Fragment {

    public FirebaseUserProfileFragment() {
        // Required empty public constructor
    }

    public static FirebaseUserProfileFragment newInstance() {
        return new FirebaseUserProfileFragment();
    }

    private TextView userInfoText;
    private CircleImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(
                R.layout.fragment_user_profile, container, false);

        userImage = view.findViewById(R.id.image_user_profile);
        userInfoText = view.findViewById(R.id.text_user_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);
        }

        if (user.getDisplayName() != null) {
            userInfoText.setText(String.format(
                    getString(R.string.user_profile_name_format), user.getDisplayName()));
        } else {
            userInfoText.setText(getString(R.string.user_profile_name_empty));
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("프로필 이미지를 바꾸시겠습니까?")
                        .setPositiveButton(R.string.change_photo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO : 사진 권한 받아서 갤러리 띄우기
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        userInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editUserNickname = new EditText(getContext());
                AlertDialog.Builder alertBuilder =
                        new AlertDialog.Builder(getContext())
                                .setTitle("닉네임 변경")
                                .setMessage("변경할 닉네임을 입력해주세요.")
                                .setPositiveButton(R.string.change_photo, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String nickname = editUserNickname.getText().toString();
                                        if (!nickname.isEmpty()) {
                                            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                            UserProfileChangeRequest profileRequest =
                                                    new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(nickname).build();

                                            firebaseUser.updateProfile(profileRequest)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                String changedNickname = firebaseUser.getDisplayName();
                                                                Log.e("닉네임 적용", changedNickname + "헤헤");

                                                                FirebaseFirestore.getInstance()
                                                                        .collection("user").document(firebaseUser.getUid())
                                                                        .update("name", changedNickname);

                                                                userInfoText.setText(changedNickname);
                                                            } else {
                                                                Log.e("닉네임 적용", "실패");
                                                            }
                                                        }
                                                    });
                                        }
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                String nickName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                if (nickName != null || !nickName.isEmpty()) {
                    editUserNickname.setText(nickName);
                }
                alertBuilder.setView(editUserNickname);
                alertBuilder.show();
            }
        });

//        ImageView imageView = view.findViewById(R.id.image_setting);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), SettingActivity.class));
//            }
//        });
        return view;
    }
}
