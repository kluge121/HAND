package com.globe.hand.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.globe.hand.R;

/**
 * Created by ssangwoo on 2017-12-21.
 */

public class HandJoinFragment extends Fragment
            implements View.OnClickListener {
    private static final String USER_EMAIL = "user_email";

    private OnCallbackJoinListener listener;

    private EditText editEmail;
    private EditText editPass;
    private EditText editNickname;

    private String userEmail;

    public HandJoinFragment() { }

    public static HandJoinFragment newInstance(String userEmail) {
        HandJoinFragment fragment = new HandJoinFragment();

        Bundle args = new Bundle();
        args.putString(USER_EMAIL, userEmail);

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

        editEmail.setText(userEmail);

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
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_btn_join:
                listener.processJoin(
                        editEmail.getText().toString(), editPass.getText().toString(),
                        editNickname.getText().toString());
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

    public interface OnCallbackJoinListener {
        void processJoin(String userEmail, String userPassword, String userNickname);
    }
}
