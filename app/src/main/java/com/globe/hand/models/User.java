package com.globe.hand.models;

import java.util.List;

/**
 * Created by baeminsu on 2017. 12. 23..
 */

public class User {

    private String name;
    private String email;
    private String gender;
    private String profile_url;
    private String uid;

    public User() {}

    public User(String name, String email, String gender, String profile_url, String uid) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.profile_url = profile_url;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
