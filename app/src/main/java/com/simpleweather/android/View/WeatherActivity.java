package com.simpleweather.android.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.simpleweather.android.R;
import com.simpleweather.android.presenters.WeatherInterface;
import com.simpleweather.android.presenters.impl.WeatherImpl;
import com.simpleweather.android.util.ContentUtil;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.forecast.AirForecast;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.alarm.Alarm;
import interfaces.heweather.com.interfacesmodule.bean.alarm.AlarmBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherActivity extends AppCompatActivity implements WeatherInterface {

    TextView nowCondText;
    TextView nowTmpText;
    TextView wfDateText;
    TextView wfCondText;
    TextView wfTmpMaxText;
    TextView wfTmpMinText;
    TextView titleCityText;
    TextView titleUpdateTimeText;
    LinearLayout forecastLayout;
    ScrollView weatherLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        HeConfig.init(ContentUtil.APK_USERNAME, ContentUtil.APK_KEY);
        HeConfig.switchToFreeServerNode();
        initView();
        initData("CN101010100");
        weatherLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 初始化数据及view
     */
    private void initData(String location) {
        WeatherImpl weatherImpl = new WeatherImpl(this, this);
        weatherImpl.getWeatherHourly(location);
        weatherImpl.getAirForecast(location);
        weatherImpl.getAirNow(location);
        //weatherImpl.getAlarm(location);
        weatherImpl.getWeatherForecast(location);
        weatherImpl.getWeatherNow(location);
    }

    private void initView() {
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCityText = (TextView) findViewById(R.id.title_city);
        titleUpdateTimeText = (TextView) findViewById(R.id.title_update_time);
        nowCondText = (TextView) findViewById(R.id.now_cond_text);
        nowTmpText = (TextView) findViewById(R.id.now_tmp_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

    }

    /**
     * 重写接口代码
     */
    @Override
    public void getWeatherNow(Now bean) {
        if (bean != null && bean.getNow() != null) {
            NowBase now = bean.getNow();
            String updateTime = bean.getUpdate().getLoc();
            String city = bean.getBasic().getLocation();
            String rain = now.getPcpn() + "mm";
            String hum = now.getHum() + "%";
            String pres = now.getPres() + "HPA";
            String vis = now.getVis() + "KM";
            String windDir = now.getWind_dir();
            String windSc = now.getWind_sc() + "级";
            String condTxt = now.getCond_txt();
            String tmp = now.getTmp() + "°";
//            condCode = now.getCond_code();
            nowTmpText.setText(tmp);
            nowCondText.setText(condTxt);
            titleCityText.setText(city);
            titleUpdateTimeText.setText(updateTime);
        }
    }

    @Override
    public void getWeatherForecast(Forecast bean) {
        if (bean != null && bean.getDaily_forecast() != null) {
            List<ForecastBase> forecastList = bean.getDaily_forecast();
            forecastLayout.removeAllViews();
            for (ForecastBase forecast : forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.items_weather_forecast,
                        forecastLayout, false);
                wfCondText = (TextView) view.findViewById(R.id.wf_cond_text);
                wfDateText = (TextView) view.findViewById(R.id.wf_date_text);
                wfTmpMaxText = (TextView) view.findViewById(R.id.wf_tmp_max_text);
                wfTmpMinText = (TextView) view.findViewById(R.id.wf_tmp_min_text);
                wfDateText.setText(forecast.getDate());
                wfCondText.setText(forecast.getCond_txt_d());
                wfTmpMaxText.setText(forecast.getTmp_max());
                wfTmpMinText.setText(forecast.getTmp_min());
                forecastLayout.addView(view);
            }
        }
    }

    @Override
    public void getAlarm(Alarm bean) {
        if (bean != null && bean.getAlarm().size() > 0 && bean.getAlarm().get(0) != null) {
            //显示警报view
            AlarmBase alarmBase = bean.getAlarm().get(0);
            String level = alarmBase.getLevel();
            String type = alarmBase.getType() + "预警";
            if (!TextUtils.isEmpty(level)) {
                //设置预警颜色
            } else {
                //隐藏警报view
            }
        }
    }

    @Override
    public void getAirNow(AirNow bean) {
        if (bean != null && bean.getAir_now_city() != null) {
            //显示相关view
            AirNowCity airNowCity = bean.getAir_now_city();
            String qlty = airNowCity.getQlty();
            String aqi = airNowCity.getAqi();
            String pm25 = airNowCity.getPm25();
            String pm10 = airNowCity.getPm10();
            String so2 = airNowCity.getSo2();
            String no2 = airNowCity.getNo2();
            String co = airNowCity.getCo();
            String o3 = airNowCity.getO3();
            //设置代码暂略
        } else {
            //隐藏相关
        }
    }

    @Override
    public void getAirForecast(AirForecast bean) {

    }

    @Override
    public void getWeatherHourly(Hourly bean) {
        if (bean != null && bean.getHourly() != null) {
            //weatherHourlyBean = bean;
            List<HourlyBase> hourlyWeatherList = bean.getHourly();
            List<HourlyBase> data = new ArrayList<>();
            if (hourlyWeatherList.size() > 23) {
                for (int i = 0; i < 24; i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getCond_code();
                    String time = data.get(i).getTime();
                    time = time.substring(time.length() - 5, time.length() - 3);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setCond_code(condCode + "d");
                    } else {
                        data.get(i).setCond_code(condCode + "n");
                    }
                }
            } else {
                for (int i = 0; i < hourlyWeatherList.size(); i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getCond_code();
                    String time = data.get(i).getTime();
                    time = time.substring(time.length() - 5, time.length() - 3);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setCond_code(condCode + "d");
                    } else {
                        data.get(i).setCond_code(condCode + "n");
                    }
                }
            }

            int minTmp = Integer.parseInt(data.get(0).getTmp());
            int maxTmp = minTmp;
            for (int i = 0; i < data.size(); i++) {
                int tmp = Integer.parseInt(data.get(i).getTmp());
                minTmp = Math.min(tmp, minTmp);
                maxTmp = Math.max(tmp, maxTmp);
            }
            //设置气温代码暂略
        }
    }

}
