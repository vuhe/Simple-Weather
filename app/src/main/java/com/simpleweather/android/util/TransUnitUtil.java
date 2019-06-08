package com.simpleweather.android.util;

public class TransUnitUtil {

    public static String getF(String value) {
        try {
            long i = Integer.parseInt(value);
            i = Math.round((i * 1.8 + 32));
            return String.valueOf(i);
        } catch (Exception e) {
            return "0";
        }
    }

}
