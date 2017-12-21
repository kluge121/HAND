package com.globe.hand.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.globe.hand.BaseActivity;
import com.globe.hand.Login.fragments.HandJoinFragment;
import com.globe.hand.Login.fragments.HandLoginFragment;
import com.globe.hand.Main.MainActivity;
import com.globe.hand.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoginActivity extends BaseActivity
        implements HandLoginFragment.OnCallbackLoginListener,
        HandJoinFragment.OnCallbackJoinListener {
    static final String TAG = LoginActivity.class.getName();

    private SessionCallback callback;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;


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

        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_container, HandLoginFragment.newInstance())
                .commit();

        initFirebase();
        initKakaoTalk();
    }

    private void initKakaoTalk() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
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
                    Bundle userInfoBundle = new Bundle();
                    userInfoBundle.putString("UID", user.getUid());
                    userInfoBundle.putString("ProviderId", user.getProviderId());
                    userInfoBundle.putString("DisplayName", user.getDisplayName());
                    if (user.getPhotoUrl() != null) {
                        userInfoBundle.putString("PhotoUrl", user.getPhotoUrl().getPath());
                    }
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
        if (Session.getCurrentSession()
                .handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void moveToJoinWithEditInfo(final String userEmail) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, HandJoinFragment.newInstance(userEmail))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void processLogin(String userEmail, String userPassword) {
        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "성공!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void processJoin(String userEmail, String userPassword, final String userNickname) {
        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            if (user != null) {
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userNickname).build());
                            }
                        }
                    }
                });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
//            String accessToken = Session.getCurrentSession()
//                    .getTokenInfo().getAccessToken();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }
}
