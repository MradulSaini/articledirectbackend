package com.db.articledirect.service;

import com.db.articledirect.Repository.ArticleRepository;
import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.Article;
import com.db.articledirect.model.ArticleHistory;
import com.db.articledirect.model.Staging;
import com.db.articledirect.utility.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Map<String, Object> addArticle(Article article, int role) {

        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        if(role == 3 && article.getStatus().equals(Constants.PUBLISHED)){
            int count = articleRepository.save(article);
            if(count != 0){
                resultMap.put("status", Constants.OK);
                resultMap.put("message", "Added Article");
            }
            else{
                resultMap.put("status", Constants.ERROR);
                resultMap.put("message", "Article not added");
            }
        }
        else{
            Staging staging = new Staging(article.getUserId(), article.getTitle(), article.getContent(), article.getDateModified(), article.getStatus());
            int count = articleRepository.saveStaging(staging);
            System.out.println("Rows updated: " + count);
            if(count != 0){
                resultMap.put("status", Constants.OK);
                resultMap.put("message", "Added Article");
            }
            else{
                resultMap.put("status", Constants.ERROR);
                resultMap.put("message", "Article not added");
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getArticleByArticleId(long id) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        Article article = articleRepository.findByArticleId(id);

        if(article != null){
            Map<String,Object> articleMap = new HashMap<>();

            articleMap.put("userId",article.getUserId());
            articleMap.put("title",article.getTitle());
            articleMap.put("content",article.getContent());
            articleMap.put("dateModified",article.getDateModified());
            articleMap.put("status",article.getStatus());


            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Fetched");
            resultMap.put("article", articleMap);

        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllArticleByUserId(String userId) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Article> res = articleRepository.findArticleByUserId(userId);

        if(res != null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Fetched");
            resultMap.put("data", res);
        }

        return resultMap;
    }
    @Override
    public Map<String, Object> getAllStArticleByUserId(String userId) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Staging> res = articleRepository.findStArticleByUserId(userId);

        if(res != null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Fetched");
            resultMap.put("data", res);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getAllArticles() {

        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Map<String, Object>> res = articleRepository.findArticles();

        if(res != null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Fetched");
            resultMap.put("data", res);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> addIntoStaging(Staging staging) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        int count = articleRepository.saveStaging(staging);
        System.out.println("Rows updated: " + count);
        if (count != 0) {
            resultMap.put("status", Constants.OK);
            resultMap.put("message", "Added/Updated Article in to Staging");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> updatePublishedArticle(Article article, int role) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");
        article.setDateModified(CommonFunctions.getCurrentDate());
        int count = 0;
        count = articleRepository.updatePubByStatus(article, role);
        if(count != 0){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Updated");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> updateStagingArticle(Staging staging) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");
        int count = 0;
        staging.setDateModified(CommonFunctions.getCurrentDate());
        count = articleRepository.updateStagedByStatus(staging);

        if(count != 0){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Updated.");
        }
        return resultMap;
    }

    //This api will be hit by super admin and will be insert/update article in article table
    @Override
    public Map<String, Object> approveStagedArticle(Staging staging) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        int count = articleRepository.approveArticle(staging);
        if(count != 0){
            resultMap.put("status", Constants.OK);
            resultMap.put("message", "Article Published.");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getArticleByTitle(String title) {
        if("".equals(title)){
            return getAllArticles();
        }
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Map<String, Object>> articles = articleRepository.findArticleByTitle(title);

        if(articles!=null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Articles Fetched by Title");
            resultMap.put("data", articles);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getArticleByDateAuthor(Map<String, String> filter) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Map<String, Object>> articles = articleRepository.findArticleBySortedDateAuthor(filter);

        if(articles!=null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Articles Fetched by sorted date in desc.");
            resultMap.put("data", articles);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllArticleByStatus(String status) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        List<Map<String, Object>> res = articleRepository.findArticleByStatus(status);

        if(res != null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Article Fetched");
            resultMap.put("data", res);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllArticleHistory(int articleId) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");
        List<ArticleHistory> articleHistory = articleRepository.findArticleHistory(articleId);
        if(articleHistory != null){
            resultMap.put("status",Constants.OK);
            resultMap.put("message", "Articles Fetched.");
            resultMap.put("data", articleHistory);
        }
        return resultMap;


    }

}
