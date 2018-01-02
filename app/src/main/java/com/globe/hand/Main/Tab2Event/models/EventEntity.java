package com.globe.hand.Main.Tab2Event.models;

/**
 * Created by baeminsu on 2017. 12. 28..
 */

public class EventEntity {
    private String category;
    private String eventName;
    private String needCount; // 3명
    private String count;  // (27/30)
    private String point;
    private String price;
    private String content;
    private String imageUrl;
    private boolean participation; // 이벤트참여여부

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setNeedCount(String needCount) {
        this.needCount = needCount;
    }

    public void setCount(String count) {
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

    public void setParticipation(boolean participation) {
        this.participation = participation;
    }

    public String getCategory() {

        return category;
    }

    public String getEventName() {
        return eventName;
    }

    public String getNeedCount() {
        return needCount;
    }

    public String getCount() {
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

    public boolean isParticipation() {
        return participation;
    }
}
