package com.db.articledirect.controller;
import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.Article;
import com.db.articledirect.model.Staging;
import com.db.articledirect.model.User;
import com.db.articledirect.service.ArticleService;
import com.db.articledirect.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    ArticleService articleService;

    @Secured({Constants.SUPER_ADMIN, Constants.ADMIN})
    @PostMapping("/addArticle")
    public Map<String, Object> addNewArticle(@RequestBody Article article, @RequestHeader (name="Authorization") String token){
        System.out.println("-------------------------Add Article----------------------");
        System.out.println("Token: "+ token.substring(7));
        User user = jwtUtility.getUserDetailsFromToken(token.substring(7));
        article.setUserId(user.getEmail());
        article.setDateModified(new java.sql.Date(System.currentTimeMillis()));

        return articleService.addArticle(article, user.getRole());
    }

    @PostMapping("/addStaging")
    public Map<String,Object> addNewIntoStaging(@RequestBody Staging staging){
        System.out.println("-------------------------Add Staging----------------------");
        return articleService.addIntoStaging(staging);
    }


    @PostMapping("/getArticle/{id}")
    public Map<String, Object> getArticleById(@PathVariable long id){
        System.out.println("-------------------------Get Article By Id----------------------");
         return articleService.getArticleByArticleId(id);
    }

    @Secured({Constants.SUPER_ADMIN, Constants.ADMIN})
    @GetMapping("/getAllArticlesByUserId")
    public Map<String, Object> getAllArticleByUserId(@RequestHeader (name="Authorization") String token){
        System.out.println("-------------------------Get All Article By UserId ----------------------");
        User user = jwtUtility.getUserDetailsFromToken(token.substring(7));
        return articleService.getAllArticleByUserId(user.getEmail());
    }

    @Secured({Constants.SUPER_ADMIN, Constants.ADMIN})
    @GetMapping("/getAllStagedArticlesByUserId")
    public Map<String, Object> getAllStagedArticleByUserId(@RequestHeader (name="Authorization") String token){
        System.out.println("-----------------Get All Staged Articles By UserId------------------------------");
        User user = jwtUtility.getUserDetailsFromToken(token.substring(7));
        return articleService.getAllStArticleByUserId(user.getEmail());
    }

    @GetMapping("/getAllArticles")
    public Map<String,Object> getAllArticle(){
        System.out.println("-----------------Get All Articles------------------------------");
        return articleService.getAllArticles();
    }

//    @Secured(Constants.SUPER_ADMIN)
    @PostMapping("/approveArticle")
    public Map<String,Object> approveArticle(@RequestBody Staging staging){
        System.out.println("-----------------Approve Articles------------------------------");
        return articleService.approveStagedArticle(staging);

    }


    @GetMapping("/getArticleByTitle")
    public Map<String,Object> getArticleByTitle(@RequestParam String searchText){
        System.out.println("-----------------Get Articles By Title------------------------------");
        return articleService.getArticleByTitle(searchText);
    }

    @GetMapping("/getArticleBySortedDateAuthor")
    public Map<String,Object> getArticleByDateAuthor(@RequestParam Map<String, String> filter){
        System.out.println("-----------------Get Articles By Sorted Date------------------------------");
        return articleService.getArticleByDateAuthor(filter);

    }

    @PostMapping("/updateStaging")
    public Map<String,Object> updateStagingArticle(@RequestBody Staging staging){
        System.out.println("-----------------Update Staged Article------------------------------");
        System.out.println(staging);
        return articleService.updateStagingArticle(staging);
    }

    @PostMapping("/updatePublish")
    public Map<String,Object> updatePublishedArticle(@RequestBody Article article, @RequestHeader (name="Authorization") String token){
        System.out.println("-----------------Update Staged Article------------------------------");
        User user = jwtUtility.getUserDetailsFromToken(token.substring(7));
        return articleService.updatePublishedArticle(article, user.getRole());
    }

    @GetMapping("/getArticleByStatus/{status}")
    public Map<String,Object> getArticleByStatus(@PathVariable String status){
        return articleService.getAllArticleByStatus(status);
    }

    @GetMapping("/getArticleHistory/{articleId}")
    public Map<String,Object> getArticleHistory(@PathVariable int articleId){
        return articleService.getAllArticleHistory(articleId);
    }
}
