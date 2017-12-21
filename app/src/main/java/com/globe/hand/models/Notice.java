package com.globe.hand.models;

import java.util.Date;

/**
 * Created by ssangwoo on 2017-12-21.
 */

public class Notice {
    String documentId;
    String title;
    String content;
    Date createTime;

    public Notice(String documentId, String title, String content, Date createTime) {
        this.documentId = documentId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
