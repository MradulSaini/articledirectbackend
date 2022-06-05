package com.db.articledirect.Repository;


import com.db.articledirect.model.User;

public interface UserRepository {
    int save(User user);
    User findByEmail(String email);
    String getAnsById(long id);
    User getUserById(Integer id);
}
