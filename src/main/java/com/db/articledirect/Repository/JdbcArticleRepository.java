package com.db.articledirect.Repository;

import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.Article;
import com.db.articledirect.model.ArticleHistory;
import com.db.articledirect.model.Staging;
import com.db.articledirect.utility.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class JdbcArticleRepository implements ArticleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int save(Article article) {
        return jdbcTemplate.update("INSERT INTO article ( userId, title, content, dateModified, status) VALUES(?,?,?,?,?)",
                new Object[]{article.getUserId(), article.getTitle(), article.getContent(), article.getDateModified(), article.getStatus()});
    }

//    @Override
//    public int saveStaging(Staging staging) {
//        System.out.println("Print the staging:" + staging);
//        if(staging.getArticleId().equalsIgnoreCase("null")){
//            return jdbcTemplate.update("INSERT INTO staging ( userId, title, content, dateModified, status) VALUES(?,?,?,?,?)",
//                    new Object[]{staging.getUserId(), staging.getTitle(), staging.getContent(), staging.getDateModified(), staging.getStatus()});
//        }else{
//            return jdbcTemplate.update("UPDATE staging set content = ? where articleId = ?",staging.getContent(),staging.getArticleId());
//        }
//    }

    @Override
    public int saveStaging(Staging staging) {
        return jdbcTemplate.update("INSERT INTO staging ( userId, title, content, dateModified, status) VALUES(?,?,?,?,?)",
                new Object[]{staging.getUserId(), staging.getTitle(), staging.getContent(), staging.getDateModified(), staging.getStatus()});
    }

    @Override
    public int saveArticleHistory(Article article) {
        return jdbcTemplate.update("INSERT INTO article_history ( userId, title, content, dateModified, status) VALUES(?,?,?,?)",
                new Object[]{article.getUserId(), article.getTitle(), article.getContent(), article.getDateModified()});
    }

    @Override
    public Article findByArticleId(long id) {
        Article article = null;

        try {
            article = jdbcTemplate.queryForObject("SELECT userId, title, content, dateModified, status FROM article WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Article.class), id);
        } catch (Exception ex) {
            System.out.println("-------------Exception in articleFindById: " + ex);
        }
        return article;
    }

    @Override
    public List<Article> findArticleByUserId(String userId) {
        List<Article> result = null;
        try {
            System.out.println(userId);
            result = jdbcTemplate.query("SELECT * FROM article WHERE userId=?",
                    (rs, rowNum) -> new Article(rs.getLong("id"), rs.getString("userId"), rs.getString("title"), rs.getString("content"), rs.getDate("dateModified"), rs.getString("status")),
                    userId);

        } catch (Exception ex) {
            System.out.println("-------------Exception in articleFindById: " + ex);
        }
        return result;
    }

    @Override
    public List<Staging> findStArticleByUserId(String userId) {
        List<Staging> result = null;
        try {
            System.out.println(userId);
            result = jdbcTemplate.query("SELECT * FROM staging WHERE userId=?",
                    (rs, rowNum) -> new Staging(rs.getLong("id"), rs.getLong("articleId"), rs.getString("userId"), rs.getString("title"), rs.getString("content"), rs.getDate("dateModified"), rs.getString("status")),
                    userId);

        } catch (Exception ex) {
            System.out.println("-------------Exception in articleFindById: " + ex);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findArticles() {
        List<Map<String, Object>> result = null;
        try {
            result = jdbcTemplate.query("SELECT article.id, article.userId, article.title, article.content, article.dateModified, article.status, user_details.name FROM article, user_details where article.userId = user_details.email",
                    (rs, rowNum) ->
                    {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", rs.getLong(1));
                        m.put("userId", rs.getString(2));
                        m.put("title", rs.getString(3));
                        m.put("content", rs.getString(4));
                        m.put("dateModified", rs.getString(5));
                        m.put("status", rs.getString(6));
                        m.put("author", rs.getString(7));
                        return m;
                    }
            );
        } catch (Exception ex) {
            System.out.println("-------------Exception in articleFindByStatus: " + ex);
        }
        return result;
    }

    @Override
    public int updatePubByStatus(Article article, int role) { // for updating published article
        if(role == 3 && article.getStatus().equalsIgnoreCase(Constants.PUBLISHED)) {
            // direct update when clicked publish - no entry in staging
            Article a = findByArticleId(article.getId());
            int count = jdbcTemplate.update("INSERT INTO article_history (articleId, userId, title, content, dateModified) values(?,?,?,?,?)",
                    article.getId(), a.getUserId(), a.getTitle(), a.getContent(), a.getDateModified());
            if(count != 0){
                return jdbcTemplate.update("UPDATE article SET content = ?, status = ?, title = ?, dateModified = ? where id = ?",
                        article.getContent(),article.getStatus(),article.getTitle(),article.getDateModified(),article.getId());
            }else{
                return 0;
            }
        }
        else{
            // save as draft, publish for others
            return jdbcTemplate.update("INSERT INTO staging ( articleId, userId, title, content, dateModified, status) VALUES(?,?,?,?,?,?)",
                    new Object[]{article.getId(), article.getUserId(), article.getTitle(), article.getContent(), article.getDateModified(), article.getStatus()});
        }
    }

    @Override
    public int updateStagedByStatus(Staging staging) {
        return jdbcTemplate.update("UPDATE staging SET content = ?, status = ?, title = ?, dateModified = ? where id = ?",
                staging.getContent(),staging.getStatus(),staging.getTitle(),staging.getDateModified(),staging.getId());
    }

    @Override
    public int approveArticle(Staging staging) {
        int count = 0;
        if (staging.getArticleId() == 0 && staging.getStatus().equalsIgnoreCase(Constants.UNDER_REVIEW)) {
            staging.setStatus(Constants.PUBLISHED);
            staging.setDateModified(CommonFunctions.getCurrentDate());

            count = jdbcTemplate.update("INSERT INTO article (userId, title, content, dateModified, status) values(?,?,?,?,?)",
                    staging.getUserId(), staging.getTitle(), staging.getContent(), staging.getDateModified(), staging.getStatus());

            if (count != 0) {
                String sql = "DELETE FROM staging WHERE id = ?";
                int temp = jdbcTemplate.update(sql, staging.getId());
            }
            return count;
        }
        else if (staging.getArticleId() != 0  && staging.getStatus().equalsIgnoreCase(Constants.UNDER_REVIEW)) {

            Article article = findByArticleId(staging.getArticleId());

            staging.setStatus(Constants.PUBLISHED);
            staging.setDateModified(CommonFunctions.getCurrentDate());
            article.setId(staging.getArticleId());

            count = 0;
            if(article != null) {
                count = jdbcTemplate.update("INSERT INTO article_history (articleId, userId, title, content, dateModified) values(?,?,?,?,?)",
                        article.getId(), article.getUserId(), article.getTitle(), article.getContent(), article.getDateModified());
                if (count != 0) {
                    staging.setDateModified(CommonFunctions.getCurrentDate());
//                    article = new Article(staging.getUserId(),staging.getTitle(),staging.getContent(),staging.getDateModified(),staging.getStatus());
//                    count = save(article);
                    count = jdbcTemplate.update("UPDATE article SET title=?, content=?, dateModified=?, status=? WHERE id=?",
                            staging.getTitle(), staging.getContent(), staging.getDateModified(), staging.getStatus(), staging.getArticleId());
                    if (count != 0) {
                        jdbcTemplate.update("DELETE FROM staging WHERE id = ?", staging.getId());
                    }
                }
            }
            return count;
        }
        return count;
    }

    public List<Map<String, Object>> findArticleByTitle(String title) {
        List<Map<String, Object>> result = null;
        try {
            result = jdbcTemplate.query("SELECT article.id, article.userId, article.title, article.content, article.dateModified, article.status, user_details.name FROM article, user_details where article.userId = user_details.email and article.title LIKE '%"+title+"%'",
                    (rs, rowNum) ->
                    {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", rs.getLong(1));
                        m.put("userId", rs.getString(2));
                        m.put("title", rs.getString(3));
                        m.put("content", rs.getString(4));
                        m.put("dateModified", rs.getString(5));
                        m.put("status", rs.getString(6));
                        m.put("author", rs.getString(7));
                        return m;
                    });
        } catch (Exception ex) {
            System.out.println("-------------Exception in Get Article By Title--------------\n" + ex);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findArticleBySortedDateAuthor(Map<String, String> filter) {
        List<Map<String, Object>> result = null;
        if(filter.get("date").equals("1") && filter.get("author").equals("0")){
            try {
                result = jdbcTemplate.query("SELECT article.id, article.userId, article.title, article.content, article.dateModified, article.status, user_details.name FROM article, user_details where article.userId = user_details.email ORDER BY article.dateModified DESC",
                        (rs, rowNum) ->
                        {
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", rs.getLong(1));
                            m.put("userId", rs.getString(2));
                            m.put("title", rs.getString(3));
                            m.put("content", rs.getString(4));
                            m.put("dateModified", rs.getString(5));
                            m.put("status", rs.getString(6));
                            m.put("author", rs.getString(7));
                            return m;
                        }
                );
            } catch (Exception ex) {
                System.out.println("-------------Exception in getArticleByTitle--------------\n" + ex);
            }
        }else if(filter.get("date").equals("0") && filter.get("author").equals("1")){
            try {
                result = jdbcTemplate.query("SELECT article.id, article.userId, article.title, article.content, article.dateModified, article.status, user_details.name FROM article, user_details where article.userId = user_details.email ORDER BY user_details.name",
                        (rs, rowNum) ->
                        {
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", rs.getLong(1));
                            m.put("userId", rs.getString(2));
                            m.put("title", rs.getString(3));
                            m.put("content", rs.getString(4));
                            m.put("dateModified", rs.getString(5));
                            m.put("status", rs.getString(6));
                            m.put("author", rs.getString(7));
                            return m;
                        }
                );
            } catch (Exception ex) {
                System.out.println("-------------Exception in getArticleByTitle--------------\n" + ex);
            }
        }
        else{
            try {
                result = jdbcTemplate.query("SELECT article.id, article.userId, article.title, article.content, article.dateModified, article.status, user_details.name FROM article, user_details where article.userId = user_details.email ORDER BY user_details.name, article.dateModified DESC",
                        (rs, rowNum) ->
                        {
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", rs.getLong(1));
                            m.put("userId", rs.getString(2));
                            m.put("title", rs.getString(3));
                            m.put("content", rs.getString(4));
                            m.put("dateModified", rs.getString(5));
                            m.put("status", rs.getString(6));
                            m.put("author", rs.getString(7));
                            return m;
                        }
                );
            } catch (Exception ex) {
                System.out.println("-------------Exception in getArticleByTitle--------------\n" + ex);
            }
        }
        return result;
    }
    

    public List<Map<String, Object>> findArticleByStatus(String status) {
        List<Map<String, Object>> result = null;
        try {
            result = jdbcTemplate.query("SELECT staging.id,staging.articleId, staging.userId, staging.title, staging.content, staging.dateModified, staging.status, user_details.name FROM staging, user_details where staging.userId = user_details.email and staging.status=?",
                    (rs, rowNum) ->
                    {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", rs.getLong(1));
                        m.put("articleId", rs.getLong(2));
                        m.put("userId", rs.getString(3));
                        m.put("title", rs.getString(4));
                        m.put("content", rs.getString(5));
                        m.put("dateModified", rs.getString(6));
                        m.put("status", rs.getString(7));
                        m.put("author", rs.getString(8));
                        return m;
                    },status);
        } catch (Exception ex) {
            System.out.println("-------------Exception in Get Article By Status--------------\n" + ex);
        }
        return result;
    }

    @Override
    public List<ArticleHistory> findArticleHistory(int articleId) {
        List<ArticleHistory> result = null;
        System.out.println(articleId);
        try {
            result = jdbcTemplate.query("SELECT * FROM article_history where articleId=?",
                    (rs, rowNum) -> new ArticleHistory(rs.getLong("id"),rs.getLong("articleId"),
                            rs.getString("userId"),rs.getString("title"),rs.getString("content"),
                            rs.getDate("dateModified")),articleId);
        } catch (Exception ex) {
            System.out.println("-------------Exception in Get Article History--------------\n" + ex);
        }
        return result;
    }
}
