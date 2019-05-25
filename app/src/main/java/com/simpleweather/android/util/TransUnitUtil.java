package com.simpleweather.android.util;

public class TransUnitUtil {

    public static String getUpdateTime(String time) {
        String[] time0 = time.split("-| ");
        String year = time0[0] + "年";
        String month = time0[1] + "月";
        if (month.charAt(0) == '0') {
            month = month.substring(1);
        }
        String day = time0[2] + "日";
        if (day.charAt(0) == '0') {
            day = day.substring(1);
        }
        return "更新时间: " + month + day + " " + time0[3];
    }

    public static String getForecastTime(String time, int index) {
        switch (index) {
            case -2:
                return "前天";
            case -1:
                return "昨天";
            case 0:
                return "今天";
            case 1:
                return "明天";
            case 2:
                return "后天";
            default:
                String[] time0 = time.split("-| ");
                String month = time0[1] + "月";
                if (month.charAt(0) == '0') {
                    month = month.substring(1);
                }
                String day = time0[2] + "日";
                if (day.charAt(0) == '0') {
                    day = day.substring(1);
                }
                return month + day;
        }
    }

    public static String getAirNowMain(String main) {
        switch (main) {
            case "PM25":
                return "PM2.5";
            case "CO":
                return "一氧化碳(CO)";
            case "O3":
                return "臭氧(O₃)";
            case "NO2":
                return "二氧化氮(NO₂)";
            case "SO2":
                return "二氧化硫(SO₂)";
            default:
                return main;
        }
    }
}
