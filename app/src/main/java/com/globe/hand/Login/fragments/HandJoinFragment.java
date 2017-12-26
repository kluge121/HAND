package com.globe.hand.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.globe.hand.R;

/**
 * Created by ssangwoo on 2017-12-21.
 */

public class HandJoinFragment extends Fragment
            implements View.OnClickListener {
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NICKNAME = "user_nickname";

    private OnCallbackJoinListener listener;

    private EditText editEmail;
    private EditText editPass;
    private EditText editNickname;
    private Spinner spinnerGender;

    private String userEmail;
    private String userNickname;

    public HandJoinFragment() { }

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
        editEmail = view.findViewById(R.id.join_edit_email);
        editPass = view.findViewById(R.id.join_edit_pass);
        editNickname = view.findViewById(R.id.join_edit_nickname);
        spinnerGender = view.findViewById(R.id.join_spinner_gender);

        editEmail.setText(userEmail);
        editNickname.setText(userNickname);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        Button btnJoin = view.findViewById(R.id.join_btn_join);
        Button btnCancel = view.findViewById(R.id.join_btn_cancel);

        btnJoin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            userEmail = getArguments().getString(USER_EMAIL);
            userNickname = getArguments().getString(USER_NICKNAME);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_btn_join:
                if(isVailedForm()) {
                    listener.processJoin(
                            editEmail.getText().toString(), editPass.getText().toString(),
                            editNickname.getText().toString(),
                            spinnerGender.getSelectedItem().toString());
                }
                break;
            case R.id.join_btn_cancel:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCallbackJoinListener) {
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
        String gender = spinnerGender.getSelectedItem().toString();

        if(email.trim().isEmpty()) {
            Toast.makeText(getContext(), "이메일을 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(pass.trim().isEmpty()) {
            Toast.makeText(getContext(), "비밀번호를 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(gender.isEmpty() || gender.equals("선택")) {
            Toast.makeText(getContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public interface OnCallbackJoinListener {
        void processJoin(String userEmail, String userPassword,
                         String userNickname, String gender);
    }
}
