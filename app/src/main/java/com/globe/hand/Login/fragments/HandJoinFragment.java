package com.globe.hand.Login.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.globe.hand.R;
import com.globe.hand.common.HandPermission;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.globe.hand.common.HandPermission.PICK_FROM_ALBUM;

/**
 * Created by ssangwoo on 2017-12-21.
 */

public class HandJoinFragment extends Fragment
        implements View.OnClickListener {
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NICKNAME = "user_nickname";

    private String imgUpLoadPath;

    private OnCallbackJoinListener listener;
    private HandPermission handPermission;

    private EditText editEmail;
    private EditText editPass;
    private EditText editNickname;

    private ImageView imageGenderWoman;
    private ImageView imageGenderMan;
    private String userGender;

    private String userEmail;
    private String userNickname;

    private CircleImageView ivProfile;

    public HandJoinFragment() {
    }

    public static HandJoinFragment newInstance(String userEmail, String userNickname) {
        HandJoinFragment fragment = new HandJoinFragment();

        Bundle args = new Bundle();
        args.putString(USER_EMAIL, userEmail);
        args.putString(USER_NICKNAME, userNickname);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_hand_join, container, false);
        ivProfile = view.findViewById(R.id.join_profile_image);
        editEmail = view.findViewById(R.id.join_edit_email);
        editPass = view.findViewById(R.id.join_edit_pass);
        editNickname = view.findViewById(R.id.join_edit_nickname);

        imageGenderMan = view.findViewById(R.id.image_switch_gender_man);
        imageGenderWoman = view.findViewById(R.id.image_switch_gender_woman);

        userGender = getString(R.string.gender);
        imageGenderMan.setOnClickListener(this);
        imageGenderWoman.setOnClickListener(this);

        ivProfile.setOnClickListener(this);

        editEmail.setText(userEmail);
        editNickname.setText(userNickname);

        Button btnJoin = view.findViewById(R.id.join_btn_join);
        btnJoin.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(USER_EMAIL);
            userNickname = getArguments().getString(USER_NICKNAME);
        }
        handPermission = new HandPermission(getContext(), this);
        handPermission.checkPhotoPermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_profile_image:
                //TODO : 이미지가져와서 뿌려줘버리기
                handPermission.doTakeAlbumAction();
                break;

            case R.id.image_switch_gender_man:
                imageGenderMan.setImageResource(R.drawable.switch_select_gender_man);
                imageGenderWoman.setImageBitmap(null);
                userGender = getString(R.string.gender_man);
                break;
            case R.id.image_switch_gender_woman:
                imageGenderMan.setImageBitmap(null);
                imageGenderWoman.setImageResource(R.drawable.switch_select_gender_woman);
                userGender = getString(R.string.gender_woman);
                break;

            case R.id.join_btn_join:
                if (isVailedForm()) {
                    listener.processJoin(
                            editEmail.getText().toString(), editPass.getText().toString(),
                            editNickname.getText().toString(),
                            userGender, imgUpLoadPath);
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCallbackJoinListener) {
            listener = (OnCallbackJoinListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public boolean isVailedForm() {
        // 임시
        String email = editEmail.getText().toString();
        String pass = editPass.getText().toString();

        if (email.trim().isEmpty()) {
            Toast.makeText(getContext(), "이메일을 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.trim().isEmpty()) {
            Toast.makeText(getContext(), "비밀번호를 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if(gender.isEmpty() || gender.equals("선택")) {
//            Toast.makeText(getContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    public interface OnCallbackJoinListener {
        void processJoin(String userEmail, String userPassword,
                         String userNickname, String gender, String profile_path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                //이미지 경로, 이름
                if (data == null) break;
                Uri selectImageUri = data.getData();

                String imageName = handPermission.getImageNameToUri(data.getData());
                Bitmap resized = handPermission.makeBitmap(selectImageUri);
                imgUpLoadPath = handPermission.saveBitmapToJpeg(
                        resized, "resizeTmp", imageName);
                ivProfile.setImageBitmap(resized);
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case IMAGE_PERMISSION_REQUEST_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    // 퍼미션 모두 허용일 시
//                } else {
//
//                }
//                break;
//        }
//    }
}
