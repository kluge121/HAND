package com.globe.hand.models;

import java.util.List;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoom {
    private String uid;
    private String PicturePath;
    private String title;
    private String desc;

    public MapRoom() {}

    public MapRoom(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public MapRoom(String title, String desc, String uid) {
        this.title = title;
        this.desc = desc;
        this.uid = uid;
    }

    public MapRoom(String uid, String picturePath,
                   String title, String desc) {
        this.uid = uid;
        this.PicturePath = picturePath;
        this.title = title;
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
