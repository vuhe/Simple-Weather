package com.simpleweather.android.presenters.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.simpleweather.android.presenters.WeatherInterface;
import com.simpleweather.android.presenters.WeatherPresenters;
import com.simpleweather.android.util.SpUtils;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.alarm.AlarmList;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherImpl implements WeatherPresenters {

    private Context context;
    private WeatherInterface weatherInterface;
    private String TAG = "sky";
    private Lang lang;
    private Unit unit;

    public WeatherImpl(Context context, WeatherInterface weatherInterface) {
        this.context = context;
        this.weatherInterface = weatherInterface;
        //测试时，仅设置中文
        lang = Lang.CHINESE_SIMPLIFIED;
        unit = Unit.METRIC;
    }

    @Override
    public void getWeatherNow(String location) {
        HeWeather.getWeatherNow(context, location, lang, unit,
                new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Now weatherNow = SpUtils.getBean(context, "weatherNow", Now.class);
                weatherInterface.getWeatherNow(weatherNow);
            }

            @Override
            public void onSuccess(Now now) {
                weatherInterface.getWeatherNow(now);
                SpUtils.saveBean(context, "weatherNow", now);
            }
        });
    }

    @Override
    public void getWeatherForecast(final String location) {
        HeWeather.getWeatherForecast(context, location, lang, unit,
                new HeWeather.OnResultWeatherForecastBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i("sky", "getWeatherForecast onError: ");
                        Forecast weatherForecast = SpUtils.getBean(context, "weatherForecast",
                                Forecast.class);
                        weatherInterface.getWeatherForecast(weatherForecast);
                        getAirForecast(location);
                    }

                    @Override
                    public void onSuccess(Forecast forecast) {
                        weatherInterface.getWeatherForecast(forecast);
                        getAirForecast(location);
                        SpUtils.saveBean(context, "weatherForecast", forecast);
                    }
                });
    }

    @Override
    public void getAlarm(String location) {
        HeWeather.getAlarm(context, location, lang, unit,
                new HeWeather.OnResultAlarmBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        weatherInterface.getAlarm(null);
                        Log.i("sky", "getAlarm onError: ");
                    }

                    @Override
                    public void onSuccess(AlarmList alarmList) {
                        weatherInterface.getAlarm(alarmList.getAlarms().get(0));
                        SpUtils.saveBean(context, "alarm", alarmList.getAlarms().get(0));
                    }
                });
    }

    @Override
    public void getAirNow(final String location) {
        HeWeather.getAirNow(context, location, lang, unit,
                new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i("sky", "getAirNow onError: ");
                        getParentAir(location);
                    }

                    @Override
                    public void onSuccess(AirNow airNow) {
                        weatherInterface.getAirNow(airNow);
                        SpUtils.saveBean(context, "airNow", airNow);
                    }
                });
    }

    private void getParentAir(String location) {
        HeWeather.getSearch(context, location, "cn,overseas", 3, lang,
                new HeWeather.OnResultSearchBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Search search) {
                        String parentCity = search.getBasic().get(0).getParent_city();
                        if (TextUtils.isEmpty(parentCity)) {
                            parentCity = search.getBasic().get(0).getAdmin_area();
                        }
                        HeWeather.getAirNow(context, parentCity, lang, unit,
                                new HeWeather.OnResultAirNowBeansListener() {
                                    @Override
                                    public void onError(Throwable throwable) {
                                        weatherInterface.getAirNow(null);
                                    }

                                    @Override
                                    public void onSuccess(AirNow airNow) {
                                        weatherInterface.getAirNow(airNow);
                                    }
                                });
                    }
                });
    }

    @Override
    public void getAirForecast(String location) {

    }

    @Override
    public void getWeatherHourly(String location) {
        HeWeather.getWeatherHourly(context, location, lang, unit,
                new HeWeather.OnResultWeatherHourlyBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i("sky", "getWeatherHourly onError: ");
                    }

                    @Override
                    public void onSuccess(Hourly hourly) {
                        weatherInterface.getWeatherHourly(hourly);
                        SpUtils.saveBean(context, "weatherHourly", hourly);
                    }
                });
    }

}
