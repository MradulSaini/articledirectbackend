package com.db.articledirect.Repository;

import com.db.articledirect.model.Article;
import com.db.articledirect.model.ArticleHistory;
import com.db.articledirect.model.Staging;

import java.util.List;
import java.util.Map;

public interface ArticleRepository {

    int save(Article article);
    int saveStaging(Staging staging);
    int saveArticleHistory(Article article);
    Article findByArticleId(long id);
    List<Article> findArticleByUserId(String userId);
    List<Staging> findStArticleByUserId(String userId);
    List<Map<String, Object>> findArticles();
    int updatePubByStatus(Article article, int role);
    int updateStagedByStatus(Staging staging);
    int approveArticle(Staging staging);
    List<Map<String, Object>> findArticleByTitle(String title);
    List<Map<String, Object>> findArticleBySortedDateAuthor(Map<String, String> filter);
    List<Map<String, Object>>findArticleByStatus(String status);
    List<ArticleHistory> findArticleHistory(int articleId);
}
