package com.globe.hand.Main.Tab3Friend.model;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class FriendEntity {


    private String imagePath;
    private String count;
    private String text;


    public FriendEntity(String imagePath, String count, String text) {
        this.imagePath = imagePath;
        this.count = count;
        this.text = text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {

        return imagePath;
    }

    public String getCount() {
        return count;
    }

    public String getText() {
        return text;
    }
}
