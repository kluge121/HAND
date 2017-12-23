package com.globe.hand.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.globe.hand.Login.fragments.LoadingFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Login.fragments.HandJoinFragment;
import com.globe.hand.Login.fragments.HandLoginFragment;
import com.globe.hand.R;
import com.globe.hand.models.FirebaseAuthToken;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends BaseActivity
        implements HandLoginFragment.OnCallbackLoginListener,
        HandJoinFragment.OnCallbackJoinListener {
    static final String TAG = LoginActivity.class.getName();

    private SessionCallback callback;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        replaceFragment(HandLoginFragment.newInstance());

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
                    redirectMainActivity(true);
                    finish();
                }
            }
        };
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, fragment)
                .commit();
    }

    @Override
    public void moveToJoinWithEditInfo(final String userEmail) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container,
                        HandJoinFragment.newInstance(userEmail, null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void processLogin(String userEmail, String userPassword) {
        replaceFragment(LoadingFragment.newInstance());
        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try{
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),
                                        "잘못된 정보를 입력하셨습니다.", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_invalid_email));
//                                mTxtEmail.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            replaceFragment(HandLoginFragment.newInstance());
                        }
                    }
                });
    }

    @Override
    public void processJoin(final String userEmail, String userPassword, final String userNickname) {
        replaceFragment(LoadingFragment.newInstance());
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
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),
                                        "비밀번호는 최소 6자리 이상", Toast.LENGTH_SHORT).show();
//                                mTxtPassword.setError(getString(R.string.error_weak_password));
//                                mTxtPassword.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),
                                        "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_invalid_email));
//                                mTxtEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(getApplicationContext(),
                                            "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_user_exists));
//                                mTxtEmail.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            replaceFragment(HandJoinFragment.newInstance(userEmail, userNickname));
                        }
                    }
                });
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
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "유저 정보 받기 실패 == " + errorResult;
                    Log.d("MainActivity", "onFailure: " + message);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    redirectLoginActivity(errorResult.toString());
                }

                @Override
                public void onNotSignedUp() {
                    // 우린 자동가입에 체크를 했으므로 onNotSignedUp 메소드가 불러질 일이
                    // 없다고 생각하면 됨
                    // redirectMainActivity();
                }

                @Override
                public void onSuccess(final UserProfile result) {
                    replaceFragment(LoadingFragment.newInstance());
                    UserProfile kakaoUserProfile = result;
                    getFirebaseJWT(kakaoUserProfile)
                            .continueWithTask(new Continuation<String, Task<AuthResult>>() {
                        @Override
                        public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                            String firebaseToken = task.getResult();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            return auth.signInWithCustomToken(firebaseToken);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "파이어 베이스 문제 : 로그인 실패", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
                redirectLoginActivity(exception.getErrorType().toString());
            }
        }
    }


    private Task<String> getFirebaseJWT(UserProfile kakaoUserProfile) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();
        HashMap<String, String> validationObject = new HashMap<>();
        validationObject.put("user_id", String.valueOf(kakaoUserProfile.getId()));
        validationObject.put("email", kakaoUserProfile.getEmail());
        validationObject.put("nickname", kakaoUserProfile.getNickname());
        validationObject.put("profile_image", kakaoUserProfile.getProfileImagePath());
        String jsonData = new GsonBuilder()
                .setLenient()
                .create().toJson(validationObject);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KakaoAuthInterface.baseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KakaoAuthInterface kakaoAuthInterface
                = retrofit.create(KakaoAuthInterface.class);
        Call<FirebaseAuthToken> call = kakaoAuthInterface.getFirebaseToken(jsonData);
        call.enqueue(new Callback<FirebaseAuthToken>() {
            @Override
            public void onResponse(Call<FirebaseAuthToken> call,
                                   Response<FirebaseAuthToken> response) {
                if(response.isSuccessful()) {
                    FirebaseAuthToken firebaseAuthToken = response.body();
                    source.setResult(firebaseAuthToken.getFirebaseToken());
                } else {
                    Log.e("LoginActivity11", "error - " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<FirebaseAuthToken> call, Throwable throwable) {
                Log.e("LoginActivity", throwable.getMessage());
            }
        });

        return source.getTask();
    }
}
