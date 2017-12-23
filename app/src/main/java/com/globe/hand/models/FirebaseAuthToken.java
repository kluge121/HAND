package com.globe.hand.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ssangwoo on 2017-12-23.
 */

public class FirebaseAuthToken {
    @SerializedName("firebase_token")
    private String firebaseToken;

    public String getFirebaseToken() {
        return firebaseToken;
    }
}
