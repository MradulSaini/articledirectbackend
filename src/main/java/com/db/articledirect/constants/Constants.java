package com.db.articledirect.constants;

public final class Constants {

    //User Roles
    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String READER = "ROLE_READER";

    //Article Status
    public static final String DRAFT = "DRAFT";
    public static final String UNDER_REVIEW = "IN_REVIEW";
    public static final String PUBLISHED = "PUBLISHED";

    //Status code
    public static final int UNAUTHORIZED = 401;
    public static final int ERROR = 500;
    public static final int OK = 200;
}
