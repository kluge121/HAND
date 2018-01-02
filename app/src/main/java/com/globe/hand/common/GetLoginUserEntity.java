package com.globe.hand.common;

import com.globe.hand.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by baeminsu on 2017. 12. 30..
 */

public class GetLoginUserEntity {

    private static FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

    public static User makeLoginUserInstance() {
        User meUser = new User();
        meUser.setUid(loginUser.getUid());
        meUser.setEmail(loginUser.getEmail());
        meUser.setName(loginUser.getDisplayName());
        meUser.setGender(null);
        if (loginUser.getPhotoUrl() != null)
            meUser.setProfile_url(loginUser.getPhotoUrl().toString());

        return meUser;
    }
}
