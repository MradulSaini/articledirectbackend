package com.db.articledirect.model;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private int role;
    private String secQues;
    private String secAns;

    public User() {
    }

    public User(long id, String name, String email, String password, int role, String secQues) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.secQues = secQues;
    }

    public User(long id, String name, String email, String password, int role, String secQues, String secAns) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.secQues = secQues;
    }

    public User(String name, String email, String password, int role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSecQues() {
        return secQues;
    }

    public void setSecQues(String secQues) {
        this.secQues = secQues;
    }

    public String getSecAns() {
        return secAns;
    }

    public void setSecAns(String secAns) {
        this.secAns = secAns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "User [name=" + name + ", id=" + id + ", password=" + password + ", email=" + email + ", role=" + role + ", ques=" + secQues + ", ans=" + secAns + "]";
    }
}
