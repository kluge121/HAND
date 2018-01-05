package com.globe.hand.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-29.
 */

public class MapRoomMember {
    private DocumentReference userReference;
    private String permission;
    private String mapRoomUid;
    private Date joinDate = new Date();

    public MapRoomMember() {}

    public MapRoomMember(DocumentReference userReference, String permission, String mapRoomUid) {
        this.userReference = userReference;
        this.permission = permission;
        this.mapRoomUid = mapRoomUid;
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

    public String getMapRoomUid() {
        return mapRoomUid;
    }

    public void setMapRoomUid(String mapRoomUid) {
        this.mapRoomUid = mapRoomUid;
    }
}
