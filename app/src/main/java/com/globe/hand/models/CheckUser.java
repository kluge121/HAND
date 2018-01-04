package com.globe.hand.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Created by baeminsu on 2017. 12. 31..
 */

public class CheckUser extends User {

    public final int ALREADY_REQUEST = 0;
    public final int ALREADY_FRIEND = 1;
    public final int NOMAL = 2;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static CheckUser transUserToCheckUser(User user, final int state) {

        CheckUser checkUser = new CheckUser();

        checkUser.setUid(user.getUid());
        checkUser.setProfile_url(user.getProfile_url());
        checkUser.setEmail(user.getEmail());
        checkUser.setGender(user.getGender());
        checkUser.setName(user.getName());
        checkUser.setState(state);


        return checkUser;
    }

    public CheckUser() {
    }
}
