package com.simpleweather.android.presenters;

public interface WeatherPresenters {

    //实况天气
    void getWeatherNow(String location);

    //3~7天天气预报
    void getWeatherForecast(String location);

    //空气实况
    void getAirNow(String location);

    //逐小时预报
    void getWeatherHourly(String location);

    //生活指数
    void getWeatherLifeStyle(String location);

}
