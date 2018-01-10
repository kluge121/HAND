package com.globe.hand.Main.Tab4Alarm.models;

import java.util.Date;

/**
 * Created by baeminsu on 2018. 1. 4..
 */

public class Notification {

    private String profile_url;
    private String Content;
    private String notiType;
    private String sendUser;
    private Date date;
    private boolean checkNoti;
    private String additionalInformation;

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getProfile_url() {
        return profile_url;
    }


    public String getContent() {
        return Content;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }


    public void setContent(String content) {
        Content = content;
    }


    public Notification() {
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {

        return date;
    }

    public boolean isCheckNoti() {
        return checkNoti;
    }

    public void setCheckNoti(boolean checkNoti) {
        this.checkNoti = checkNoti;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAdditionalInformation() {

        return additionalInformation;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getSendUser() {
        return sendUser;
    }
}
