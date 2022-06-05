package com.db.articledirect.controller;

import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.*;
import com.db.articledirect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Map<String, Object> userRegistration(@RequestBody User user){
        System.out.println("-------------------------REGISTER----------------------");
        return userService.register(user);
    }

    @PostMapping("/login")
    public Map<String, Object> userLogin(@RequestBody User user){
        System.out.println("-------------------------LOGIN----------------------");
        try{
            return userService.login(user);
        }
        catch (Exception ex){
            Map<String, Object> resultMap =  new HashMap<>();
            resultMap.put("status", Constants.ERROR);
            resultMap.put("message", "Exception occurred");
            return resultMap;
        }
    }

    @PostMapping("/securityCheck")
    public Map<String, Object> securityCheck(@RequestBody User user){
        System.out.println("------------------------SECURITY CHECK--------------------------");
        return userService.checkSecurityAns(user);
    }

    @Secured(Constants.ADMIN)
    @GetMapping("/debugJWT")
    public String debugJWT(){
        System.out.println("----------------------------DEBUGGING JWT-----------------------------");
        return "Success";
    }
}
