package com.db.articledirect.model;

import java.sql.Date;


public class Article {

    private long id;
    private String userId;
    private String title;
    private String content;
    private Date dateModified;
    private String status;

    public Article(){}

    public Article(long id, String userId, String title, String content, Date dateModified, String status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.dateModified = dateModified;
        this.status = status;
    }

    public Article(String userId, String title, String content, Date dateModified, String status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.dateModified = dateModified;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", dateModified=" + dateModified +
                ", status='" + status + '\'' +
                '}';
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
