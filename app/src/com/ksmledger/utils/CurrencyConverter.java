package com.ksmledger.utils;

import java.text.NumberFormat;
import java.util.*;
public class CurrencyConverter {
    public static String ngn(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
        return formatter.format(amount);
    }
}
