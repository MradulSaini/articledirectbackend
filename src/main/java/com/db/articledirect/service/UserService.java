package com.db.articledirect.service;
import com.db.articledirect.model.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(User user);
    Map<String, Object> register(User user);
    Map<String, Object> checkSecurityAns(User u);
}
