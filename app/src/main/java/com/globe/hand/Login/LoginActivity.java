package com.globe.hand.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.support.annotation.NonNull;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.globe.hand.BaseActivity;
import com.globe.hand.Main.MainActivity;
import com.globe.hand.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoginActivity extends BaseActivity {

    private TextView textHandLogo;
    private SessionCallback callback;
    static final String TAG = LoginActivity.class.getName();

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    CallbackManager mFacebookCallbackManager;


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null)
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textHandLogo = findViewById(R.id.text_hand_logo);

//        LoginButton kakaoLoginButton = findViewById(R.id.button_kakao_login);

        initFirebase();
        initFacebook();
        initKakaoTalk();
    }

    private void initKakaoTalk() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initFacebook() {
        mFacebookCallbackManager = CallbackManager.Factory.create();

        com.facebook.login.widget.LoginButton mSigninFacebookButton
                = findViewById(R.id.sign_in_facebook_button);
        mSigninFacebookButton.setReadPermissions("email", "public_profile");
        mSigninFacebookButton.registerCallback(
                mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider
                        .getCredential(loginResult.getAccessToken().getToken());
                mFirebaseAuth.signInWithCredential(credential);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook Login Error", error);
            }
        });
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "sign in");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("facebook", true);

                    Bundle userInfoBundle = new Bundle();
                    userInfoBundle.putString("UID", user.getUid());
                    userInfoBundle.putString("ProviderId", user.getProviderId());
                    userInfoBundle.putString("DisplayName", user.getDisplayName());
                    userInfoBundle.putString("PhotoUrl", user.getPhotoUrl().getPath());
                    userInfoBundle.putString("Email", user.getEmail());
                    userInfoBundle.putString("PhoneNumber", user.getPhoneNumber());
                    intent.putExtra("user_info", userInfoBundle);

                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "sign out");
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession()
                .handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
//            String accessToken = Session.getCurrentSession()
//                    .getTokenInfo().getAccessToken();
            // TODO : 토큰을 서버를 하나 파서 만들어야하네!?
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }
}
