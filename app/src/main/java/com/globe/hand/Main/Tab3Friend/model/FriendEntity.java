package com.globe.hand.Main.Tab3Friend.model;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendEntity {

    private String imagePath;
    private String email;
    private String name;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {

        return imagePath;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
