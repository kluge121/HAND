package com.globe.hand.Main.Tab2Event.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by baeminsu on 2018. 1. 4..
 */

public class EventRefEntity {


    private String uid;
    private DocumentReference eventRef;


    public EventRefEntity() {
    }

    public EventRefEntity(EventEntity model) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventRef = db.collection("event").document(model.getUid());
        this.uid = model.getUid();

    }

    public String getUid() {
        return uid;
    }

    public DocumentReference getEventRef() {
        return eventRef;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEventRef(DocumentReference eventRef) {
        this.eventRef = eventRef;
    }
}
