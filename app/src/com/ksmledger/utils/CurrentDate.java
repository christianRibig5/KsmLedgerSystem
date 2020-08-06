package com.ksmledger.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CurrentDate {
    public static String getCurrentDate(){
        //getting current date using Date class
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateObj = new Date();
        return df.format(dateObj);
    }
}
