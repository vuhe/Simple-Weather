package com.simpleweather.android.presenters;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;

public interface WeatherInterface {

    //实况天气
    void getWeatherNow(Now bean);

    //3~7天天气预报
    void getWeatherForecast(Forecast bean);

    //空气实况
    void getAirNow(AirNow bean);

    //逐小时预报
    void getWeatherHourly(Hourly bean);

    //生活指数
    void getWeatherLifeStyle(Lifestyle bean);

}
