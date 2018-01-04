package com.globe.hand.Main.Tab2Event.models;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventEntity {
    private String uid;
    private String category;
    private String eventName;
    private int currentCount;
    private int count;
    private String point;
    private String price;
    private String content;
    private String imageUrl;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {

        return uid;
    }

    public String getCategory() {
        return category;
    }

    public String getEventName() {
        return eventName;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getCount() {
        return count;
    }

    public String getPoint() {
        return point;
    }

    public String getPrice() {
        return price;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public EventEntity() {
    }
}
