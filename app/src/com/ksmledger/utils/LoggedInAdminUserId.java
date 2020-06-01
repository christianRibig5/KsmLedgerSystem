package com.ksmledger.utils;

public class LoggedInAdminUserId {
    private static int userId;
    public void setLoggedAdminUserId(int rowId){
        this.userId =rowId;
    }
    public static int getUserId(){
        return userId;
    }
}
