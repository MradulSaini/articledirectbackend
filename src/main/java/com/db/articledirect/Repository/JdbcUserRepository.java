package com.db.articledirect.Repository;

import com.db.articledirect.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Types;

@Repository
public class JdbcUserRepository implements UserRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int save(User user) {
        System.out.println(user);
        return jdbcTemplate.update("INSERT INTO user_details ( email, name, password, role, secQues, secAns ) VALUES(?,?,?,?,?,?)",
                new Object[]{user.getEmail(), user.getName(), user.getPassword(), user.getRole(), user.getSecQues(), user.getSecAns()});
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try{
            user = jdbcTemplate.queryForObject("SELECT id, email, name, password, role, secQues from user_details where email=?",
                    BeanPropertyRowMapper.newInstance(User.class), email);
            return user;
        }
        catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    @Override
    public String getAnsById(long id) {
        return jdbcTemplate.queryForObject("SELECT secAns FROM user_details WHERE id=?", new Object[]{ id }, new int[]{Types.INTEGER}, String.class);
    }

    @Override
    public User getUserById(Integer id) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject("SELECT email, name, role, password from user_details where id=?",
                    BeanPropertyRowMapper.newInstance(User.class), id);
        }
        catch (Exception ex){
            System.out.println("-----Exception in getUserByID--------"+ ex.getMessage());
        }
        return user;
    }
}
