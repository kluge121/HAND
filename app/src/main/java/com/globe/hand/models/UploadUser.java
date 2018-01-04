package com.globe.hand.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by baeminsu on 2018. 1. 3..
 */

public class UploadUser {

    private String uid;
    private DocumentReference userRef;

    public UploadUser(User model) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = db.collection("user").document(model.getUid());
        uid = model.getUid();
    }

    public UploadUser() {
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
