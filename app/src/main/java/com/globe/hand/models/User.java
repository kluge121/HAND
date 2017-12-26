package com.globe.hand.models;

import android.net.Uri;

/**
 * Created by baeminsu on 2017. 12. 23..
 */

public class User {

    private String name;
    private String email;
    private Uri profile_url;
    private String uid;


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfile_url(Uri profile_url) {
        this.profile_url = profile_url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {
        return email;
    }

    public Uri getProfile_url() {
        return profile_url;
    }

    public String getUid() {
        return uid;
    }
}
