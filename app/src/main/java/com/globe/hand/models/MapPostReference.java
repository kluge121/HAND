package com.globe.hand.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

/**
 * Created by ssangwoo on 2018-01-04.
 */

public class MapPostReference {
    private DocumentReference mapPostReference;
    private String category;
    private String authorUid;
    private Date createTime = new Date();

    public MapPostReference() {}

    public MapPostReference(DocumentReference mapPostReference, String category, String authorUid) {
        this.mapPostReference = mapPostReference;
        this.category = category;
        this.authorUid = authorUid;
    }

    public DocumentReference getMapPostReference() {
        return mapPostReference;
    }

    public void setMapPostReference(DocumentReference mapPostReference) {
        this.mapPostReference = mapPostReference;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }
}
