package com.db.articledirect.service;

import com.db.articledirect.Repository.UserRepository;
import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.User;
import com.db.articledirect.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtility jwtUtility;

    @Override
    public Map<String, Object> login(User user) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        User u = userRepository.findByEmail(user.getEmail());
        if(u != null) {
            String dbPass = u.getPassword();
            int dbRole = u.getRole();

            String inPass = user.getPassword();
            int inRole = user.getRole();

            if (dbPass.equals(inPass)) {
                if (dbRole == inRole) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("name", u.getName());
                    dataMap.put("id", u.getId());
                    dataMap.put("email", u.getEmail());
                    dataMap.put("role", u.getRole());
                    dataMap.put("secQues", u.getSecQues());

                    resultMap.put("status", Constants.OK);
                    resultMap.put("message", "Login Successful");
                    resultMap.put("data", dataMap);
                    return resultMap;
                } else {
                    resultMap.put("status", Constants.UNAUTHORIZED);
                    resultMap.put("message", "Unauthorized Access");
                    return resultMap;
                }
            } else {
                resultMap.put("status", Constants.UNAUTHORIZED);
                resultMap.put("message", "Invalid Credentials");
                return resultMap;
            }
        }
        else{
            resultMap.put("status", Constants.UNAUTHORIZED);
            resultMap.put("message", "Invalid Credentials");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");
        User u = null;
        try {
            u = userRepository.findByEmail(user.getEmail());
        }
        catch(EmptyResultDataAccessException ex){
            System.out.println("Email don't exist, creating new user");
        }
        if(u != null){
            resultMap.put("status", Constants.UNAUTHORIZED);
            resultMap.put("message", "User already exists");
        }
        else {
            int count = userRepository.save(user);
            System.out.println("Rows updated: " + count);
            if (count != 0) {
                resultMap.put("status", Constants.OK);
                resultMap.put("message", "Added user");
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> checkSecurityAns(User u) {
        Map<String, Object> resultMap =  new HashMap<>();
        resultMap.put("status", Constants.ERROR);
        resultMap.put("message", "Error while fetching data from database");

        String ans = userRepository.getAnsById(u.getId());
        if(ans.equalsIgnoreCase(u.getSecAns())){
            resultMap.put("status", Constants.OK);
            resultMap.put("message", "Security check passed");

            String token = jwtUtility.generateToken(u);
            resultMap.put("token", token);
        }
        else{
            resultMap.put("status", Constants.UNAUTHORIZED);
            resultMap.put("message", "Security check NOT passed");
        }
        return resultMap;
    }
}
