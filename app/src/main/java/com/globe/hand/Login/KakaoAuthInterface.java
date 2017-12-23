package com.globe.hand.Login;

import com.globe.hand.models.FirebaseAuthToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ssangwoo on 2017-12-23.
 */

public interface KakaoAuthInterface {

    String baseURL = "https://us-central1-sa-project-168112.cloudfunctions.net/api/";

    @Headers("Content-Type: application/json")
    @POST("verifyKakao")
    Call<FirebaseAuthToken> getFirebaseToken(@Body String body);
}
