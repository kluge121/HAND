package com.globe.hand.models;

import android.net.Uri;

/**
 * Created by baeminsu on 2017. 12. 23..
 */

public class User {


    public User() {
    }

    private String name;
    private String email;
    private String gender;
    private String profile_url;
    private String uid;


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {

        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getEmail() {

        return email;
    }

    public String getProfile_url() {
        return profile_url;

    }
}
