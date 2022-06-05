package com.db.articledirect.model;

import java.sql.Date;

public class ArticleHistory {

    private long id;
    private long articleId;
    private String userId;
    private String title;
    private String content;
    private Date dateModified;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
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


    public ArticleHistory() {
    }

    public ArticleHistory(long id, long articleId, String userId, String title, String content, Date dateModified) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.dateModified = dateModified;
    }
}
