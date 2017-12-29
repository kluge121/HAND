package com.globe.hand;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.FacebookSdk;
import com.globe.hand.common.GoogleApiHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.firebase.FirebaseApp;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by ssangwoo on 2017-12-15.
 */

public class App extends Application {

    private GoogleApiHelper googleApiHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSDK.init(new KaKaoSDKAdapter());

        FacebookSdk.sdkInitialize(getApplicationContext());

        FirebaseApp.initializeApp(this);

        googleApiHelper = new GoogleApiHelper(this);
    }

    private static class KaKaoSDKAdapter extends KakaoAdapter {
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return true;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return App.getAppContext();
                }
            };
        }
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getAppContext().googleApiHelper;
    }

    // 싱글턴
    private static volatile App instance = null;
    public static App getAppContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.globe.hand.App");
        return instance;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
        googleApiHelper.disconnect();
    }
}
