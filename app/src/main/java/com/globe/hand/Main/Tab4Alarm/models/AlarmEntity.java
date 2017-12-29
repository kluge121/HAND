package com.globe.hand.Main.Tab4Alarm.models;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class AlarmEntity {

    private String profile_url;
    private String name;
    private String cotent;

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCotent(String cotent) {
        this.cotent = cotent;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getName() {
        return name;
    }

    public String getCotent() {
        return cotent;
    }
}
