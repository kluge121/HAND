package com.globe.hand.models;

/**
 * Created by ssangwoo on 2018-01-03.
 */

public class Category {
    private String uid;
    private String mapRoomUid;
    private String name;

    public Category() {}

    public Category(String mapRoomUid, String name) {
        this.mapRoomUid = mapRoomUid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMapRoomUid() {
        return mapRoomUid;
    }

    public void setMapRoomUid(String mapRoomUid) {
        this.mapRoomUid = mapRoomUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
