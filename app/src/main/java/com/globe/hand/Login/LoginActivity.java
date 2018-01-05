package com.globe.hand.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.globe.hand.Login.fragments.LoadingFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.Login.fragments.HandJoinFragment;
import com.globe.hand.Login.fragments.HandLoginFragment;
import com.globe.hand.R;
import com.globe.hand.common.RetrofitHelper;
import com.globe.hand.enums.MapRoomPermission;
import com.globe.hand.models.FirebaseAuthToken;
import com.globe.hand.models.JoinedMapRooms;
import com.globe.hand.models.MapRoom;
import com.globe.hand.models.MapRoomMember;
import com.google.android.gms.tasks.Continuation;
import com.globe.hand.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.GsonBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity
        implements HandLoginFragment.OnCallbackLoginListener,
        HandJoinFragment.OnCallbackJoinListener {
    static final String TAG = LoginActivity.class.getName();

    private SessionCallback callback;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private FirebaseFirestore db;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbar(R.id.login_toolbar, false);
        setToolbarTitle("login");
        replaceFragment(HandLoginFragment.newInstance());

        initFirebase();
        initKakaoTalk();
    }

    private void initKakaoTalk() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    redirectMainActivity();
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
        setToolbar(R.id.login_toolbar, true);
        setToolbarTitle("Sign in");
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
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),
                                        "잘못된 정보를 입력하셨습니다.", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_invalid_email));
//                                mTxtEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            replaceFragment(HandLoginFragment.newInstance());
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

    @Override
    public void processJoin(final String userEmail, String userPassword,
                            final String userNickname, final String gender,
                            final String profile_path) {
        replaceFragment(LoadingFragment.newInstance());

        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            if (profile_path != null) {
                                Uri file = Uri.fromFile(new File(profile_path));
                                StorageReference profileImgRef = storageRef.child("profile_image/" + firebaseUser.getUid());
                                UploadTask uploadTask = profileImgRef.putFile(file);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Uri profile_URI = taskSnapshot.getDownloadUrl();

                                        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setPhotoUri(profile_URI).build());

                                        db.collection("user").document(firebaseUser.getUid())
                                                .update("profile_url", profile_URI.toString());
                                    }
                                });
                            }

                            UserProfileChangeRequest profileRequest
                                    = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userNickname)
                                    .build();
                            firebaseUser.updateProfile(profileRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            createUserAndMyMapRoom(gender, null);
                                        }
                                    });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),
                                        "비밀번호는 최소 6자리 이상", Toast.LENGTH_SHORT).show();
//                                mTxtPassword.setError(getString(R.string.error_weak_password));
//                                mTxtPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),
                                        "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_invalid_email));
//                                mTxtEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(),
                                        "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
//                                mTxtEmail.setError(getString(R.string.error_user_exists));
//                                mTxtEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            replaceFragment(HandJoinFragment.newInstance(userEmail, userNickname));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setToolbarTitle("login");
    }

    private void createUserAndMyMapRoom(String gender, String profile_url) {
        final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        User user = new User();
        user.setName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        if(gender != null) {
            user.setGender(gender);
        }
        if(profile_url != null) {
            user.setProfile_url(profile_url);
        }
        user.setUid(firebaseUser.getUid());

        db.collection("user").document(firebaseUser.getUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 자신의 맵룸 추가
                            MapRoom mapRoom = new MapRoom("My hand", "나의 글",
                                    firebaseUser.getUid());
                            DocumentReference myMapRoomRef =
                                    db.collection("map_room").document(firebaseUser.getUid());

                            myMapRoomRef.set(mapRoom);

                            // 참여한 맵룸 목록 추가
                            db.collection("map_room").document(firebaseUser.getUid())
                                    .collection("joined_map_rooms").document(firebaseUser.getUid())
                                    .set(new JoinedMapRooms(myMapRoomRef));
                        }
                    }
                });
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
                    redirectLoginActivity(LoginActivity.this, errorResult.toString());
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
                    getFirebaseJWT(result).continueWithTask(new Continuation<String, Task<AuthResult>>() {
                        @Override
                        public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                            return mFirebaseAuth.signInWithCustomToken(task.getResult());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createUserAndMyMapRoom(null, result.getProfileImagePath());
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "파이어 베이스 문제 : 로그인 실패", Toast.LENGTH_SHORT)
                                        .show();
                                Log.e("카톡로그인실패", task.getException().getMessage(),
                                        task.getException());
                                replaceFragment(HandLoginFragment.newInstance());
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
                redirectLoginActivity(LoginActivity.this,
                        exception.getErrorType().toString());
            }
        }
    }


    private Task<String> getFirebaseJWT(UserProfile kakaoUserProfile) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();
        HashMap<String, String> validationObject = new HashMap<>();
        validationObject.put("user_id", String.valueOf(kakaoUserProfile.getId()));
        validationObject.put("email", kakaoUserProfile.getEmail());
        validationObject.put("nickname", kakaoUserProfile.getNickname());
        validationObject.put("profile_image", kakaoUserProfile.getThumbnailImagePath());
        String jsonData = new GsonBuilder()
                .setLenient()
                .create().toJson(validationObject);

        RetrofitHelper retrofitHelper = new RetrofitHelper(KakaoAuthInterface.baseURL);
        KakaoAuthInterface kakaoAuthInterfaceService
                = retrofitHelper.getRetrofit().create(KakaoAuthInterface.class);
        kakaoAuthInterfaceService.getFirebaseToken(jsonData)
                .enqueue(new Callback<FirebaseAuthToken>() {
                    @Override
                    public void onResponse(Call<FirebaseAuthToken> call,
                                           Response<FirebaseAuthToken> response) {
                        if (response.isSuccessful()) {
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
