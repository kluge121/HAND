package com.globe.hand.models;

/**
 * Created by ssangwoo on 2017-12-20.
 */

public class MapRoom {
    private int id;
    private int PicturePath;
    private String title;

    public MapRoom(int id, int picturePath, String title) {
        this.id = id;
        PicturePath = picturePath;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(int picturePath) {
        PicturePath = picturePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
