package com.globe.hand.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-29.
 */

public class MapRoomMember {
    DocumentReference userReference;
    String permission;
    Date joinDate;

    public MapRoomMember(DocumentReference userReference, String permission, Date joinDate) {
        this.userReference = userReference;
        this.permission = permission;
        this.joinDate = joinDate;
    }

    public DocumentReference getUserReference() {
        return userReference;
    }

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
