package com.globe.hand.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-30.
 */

public class MapPost {
    private String uid;
    private String authorUid;
    private GeoPoint geoPoint;
    private String title;
    private String content;
    private String imageUrl;
    private Date createTime = new Date();
    private Date modifiedTime = new Date();

    public MapPost() {}

    public MapPost(String authorUid, GeoPoint geoPoint, String title, String content, String imageUrl) {
        this.authorUid = authorUid;
        this.geoPoint = geoPoint;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
