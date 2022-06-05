package com.db.articledirect.service;
import com.db.articledirect.model.Article;
import com.db.articledirect.model.Staging;
import java.util.Map;

public interface ArticleService {
    Map<String, Object> addArticle(Article article, int role);
    Map<String, Object> getArticleByArticleId(long id);
    Map<String, Object> getAllArticleByUserId(String userId);
    Map<String, Object> getAllStArticleByUserId(String userId);
    Map<String, Object> getAllArticles();
    Map<String, Object> addIntoStaging(Staging staging);
    Map<String, Object> updatePublishedArticle(Article article, int role);
    Map<String, Object> updateStagingArticle(Staging staging);
    Map<String, Object> approveStagedArticle(Staging staging);
    Map<String,Object> getArticleByTitle(String title);
    Map<String, Object> getArticleByDateAuthor(Map<String, String> filter);
    Map<String,Object> getAllArticleByStatus(String status);
    Map<String,Object> getAllArticleHistory(int articleId);
}
