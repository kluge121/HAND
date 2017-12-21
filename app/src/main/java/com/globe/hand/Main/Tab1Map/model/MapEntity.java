package com.globe.hand.Main.Tab1Map.model;

/**
 * Created by baeminsu on 2017. 12. 21..
 */

public class MapEntity {


    public MapEntity(String imagePath, String text) {
        this.imagePath = imagePath;
        this.text = text;
    }

    private String imagePath;
    private String text;

    public String getImagePath() {
        return imagePath;
    }

    public String getText() {
        return text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setText(String text) {
        this.text = text;
    }
}
