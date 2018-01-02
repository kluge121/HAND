package com.globe.hand.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

/**
 * Created by ssangwoo on 2018-01-01.
 */

public class JoinedMapRooms {
    private DocumentReference mapRoomReference;
    private Date joinedDate = new Date();

    public JoinedMapRooms(DocumentReference mapRoomReference) {
        this.mapRoomReference = mapRoomReference;
    }

    public DocumentReference getMapRoomReference() {
        return mapRoomReference;
    }

    public void setMapRoomReference(DocumentReference mapRoomReference) {
        this.mapRoomReference = mapRoomReference;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }
}
