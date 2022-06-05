package com.db.articledirect.utility;

import java.sql.Date;

public class CommonFunctions {

    public static Date getCurrentDate(){
        return new java.sql.Date(System.currentTimeMillis());
    }
}
