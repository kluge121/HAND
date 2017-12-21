package com.globe.hand.Login.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.globe.hand.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class HandLoginFragment extends Fragment
        implements View.OnClickListener{

    private TextView textHandLogo;
    private EditText editEmail;
    private EditText editPass;
    private Button btnJoin;
    private Button btnLogin;

    CallbackManager mFacebookCallbackManager;

    private OnCallbackLoginListener listener;

    public HandLoginFragment() {
        // Required empty public constructor
    }

    public static HandLoginFragment newInstance() {
        return new HandLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_hand_login, container, false);

        textHandLogo = view.findViewById(R.id.text_hand_logo);
        editEmail = view.findViewById(R.id.login_edit_email);
        editPass = view.findViewById(R.id.login_edit_pass);
        btnJoin = view.findViewById(R.id.login_btn_join);
        btnLogin = view.findViewById(R.id.login_btn_login);

        initFacebook(view);

        btnLogin.setOnClickListener(this);
        btnJoin.setOnClickListener(this);

        return view;
    }

    private void initFacebook(View view) {
        mFacebookCallbackManager = CallbackManager.Factory.create();

        com.facebook.login.widget.LoginButton mSigninFacebookButton
                = view.findViewById(R.id.sign_in_facebook_button);
        mSigninFacebookButton.setReadPermissions("email", "public_profile");
        mSigninFacebookButton.registerCallback(
                mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential credential = FacebookAuthProvider
                                .getCredential(loginResult.getAccessToken().getToken());
                        FirebaseAuth.getInstance().signInWithCredential(credential);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginActivity", "Facebook login canceled.");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("LoginActivity", "Facebook Login Error", error);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                listener.processLogin(
                        editEmail.getText().toString(), editPass.getText().toString());
                break;
            case R.id.login_btn_join:
                listener.moveToJoinWithEditInfo(editEmail.getText().toString());
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCallbackLoginListener) {
            listener = (OnCallbackLoginListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnCallbackLoginListener {
        void moveToJoinWithEditInfo(String userEmail);
        void processLogin(String userEmail, String userPassword);
    }
}
