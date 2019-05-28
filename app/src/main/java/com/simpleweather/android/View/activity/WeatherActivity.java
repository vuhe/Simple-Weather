package com.simpleweather.android.View.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simpleweather.android.MyApplication;
import com.simpleweather.android.R;
import com.simpleweather.android.View.horizon_view.HourlyForecastView;
import com.simpleweather.android.View.horizon_view.IndexHorizontalScrollView;
import com.simpleweather.android.View.horizon_view.ScrollWatched;
import com.simpleweather.android.View.horizon_view.ScrollWatcher;
import com.simpleweather.android.presenters.WeatherInterface;
import com.simpleweather.android.presenters.impl.WeatherImpl;
import com.simpleweather.android.util.ContentUtil;
import com.simpleweather.android.util.IconUtils;
import com.simpleweather.android.util.SpUtils;
import com.simpleweather.android.util.TransUnitUtil;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherActivity extends AppCompatActivity implements WeatherInterface {

    private NestedScrollView weatherLayout;
    private CoordinatorLayout weatherBackground;
    private Toolbar title;
    private SwipeRefreshLayout weatherRefresh;

    private TextView nowCondText;
    private TextView nowTmpText;
    private TextView nowFlText;
    private TextView nowWindDirText;
    private TextView nowWindSpdText;
    private TextView nowWindScText;
    private TextView nowHumText;
    private TextView nowPcpnText;
    private TextView nowPresText;
    private TextView nowVisText;
    private TextView nowCloudText;
    private ImageView nowCondIcon;

    private LinearLayout forecastLayout;
    private TextView wfDateText;
    private TextView wfCondText;
    private ImageView wfCondIcon;
    private TextView wfTmpText;

    List<ScrollWatcher> watcherList;
    private List<TextView> textViewList = new ArrayList<>();
    private TextView tvLineMin;
    private TextView tvLineMax;
    private Hourly weatherHourlyBean;
    private CardView hourlyCard;
    private ScrollWatched watched;
    private HourlyForecastView hourlyForecastView;

    private TextView airNowAqiText;
    private TextView airNowMainText;
    private TextView airNowQltyText;
    private TextView airNowPm10Text;
    private TextView airNowPm25Text;
    private TextView airNowNo2Text;
    private TextView airNowSo2Text;
    private TextView airNowCoText;
    private TextView airNowO3Text;
    private CardView airNowCard;

    private TextView lifeStyleBrfText;
    private TextView lifeStyleTypeText;
    private CardView lifeStyleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        HeConfig.init(ContentUtil.APK_USERNAME, ContentUtil.APK_KEY);
        HeConfig.switchToFreeServerNode();
        initObserver();
        initView();
        String mLocation = SpUtils.getString("cityId");
        if (mLocation != null && !mLocation.equals(ContentUtil.LOCATION)) {
            ContentUtil.LOCATION = mLocation;
        }
        initData(ContentUtil.LOCATION);
        weatherLayout.setVisibility(View.VISIBLE);
    }



    /**
     * 返回活动时，刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        String mLocation = SpUtils.getString("cityId");
        if (mLocation != null && !mLocation.equals(ContentUtil.LOCATION)) {
            ContentUtil.LOCATION = mLocation;
            initData(ContentUtil.LOCATION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mLocation = SpUtils.getString("cityId");
        if (mLocation != null && !mLocation.equals(ContentUtil.LOCATION)) {
            ContentUtil.LOCATION = mLocation;
            initData(ContentUtil.LOCATION);
        }
    }

    /**
     * 初始化横向滚动条的监听
     */
    private void initObserver() {
        watcherList = new ArrayList<>();
        watched = new ScrollWatched() {
            @Override
            public void addWatcher(ScrollWatcher watcher) {
                watcherList.add(watcher);
            }

            @Override
            public void removeWatcher(ScrollWatcher watcher) {
                watcherList.remove(watcher);
            }

            @Override
            public void notifyWatcher(int x) {
                for (ScrollWatcher watcher : watcherList) {
                    watcher.update(x);
                }
            }
        };
    }

    /**
     * 初始化免费节点全部数据及view
     */
    private void initData(String location) {
        WeatherImpl weatherImpl = new WeatherImpl(this);
        weatherImpl.getWeatherNow(location);
        weatherImpl.getWeatherHourly(location);
        weatherImpl.getAirNow(location);
        weatherImpl.getWeatherForecast(location);
        weatherImpl.getWeatherLifeStyle(location);
    }

    /**
     * 设置view及menu
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_city_search:
                Intent intent = new Intent(WeatherActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.title_setting:
                //启动设置
                break;
            default:
                break;
        }
        return true;
    }

    private void initView() {

        //标题view初始化
        weatherBackground = findViewById(R.id.weather_background);
        weatherLayout = findViewById(R.id.weather_layout);
        title = findViewById(R.id.title_toolbar);
        setSupportActionBar(title);
        weatherRefresh = findViewById(R.id.weather_refresh);
        weatherRefresh.setColorSchemeResources(R.color.colorPrimary);
        weatherRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(ContentUtil.LOCATION);
                weatherRefresh.setRefreshing(false);
            }
        });

        //实况天气view初始化
        nowCondText = findViewById(R.id.now_cond_text);
        nowTmpText = findViewById(R.id.now_tmp_text);
        nowFlText = findViewById(R.id.now_fl_text);
        nowWindDirText = findViewById(R.id.now_wind_dir_text);
        nowWindSpdText = findViewById(R.id.now_wind_spd_text);
        nowWindScText = findViewById(R.id.now_wind_sc_text);
        nowHumText = findViewById(R.id.now_hum_text);
        nowPcpnText = findViewById(R.id.now_pcpn_text);
        nowPresText = findViewById(R.id.now_pres_text);
        nowVisText = findViewById(R.id.now_vis_text);
        nowCloudText = findViewById(R.id.now_cloud_text);
        nowCondIcon = findViewById(R.id.now_cond_icon);

        //3~7天天气预报view初始化
        forecastLayout = findViewById(R.id.forecast_layout);

        //小时天气view初始化
        hourlyCard = findViewById(R.id.hourly_card);
        IndexHorizontalScrollView horizontalScrollView = findViewById(R.id.hsv);
        hourlyForecastView = findViewById(R.id.hourly);
        horizontalScrollView.setToday24HourView(hourlyForecastView);
        watched.addWatcher(hourlyForecastView);
        horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                watched.notifyWatcher(scrollX);
            }
        });
        tvLineMin = findViewById(R.id.tv_line_min_tmp);
        textViewList.add(tvLineMin);
        tvLineMax = findViewById(R.id.tv_line_max_tmp);
        textViewList.add(tvLineMax);

        //空气质量实况view初始化
        airNowAqiText = findViewById(R.id.air_now_aqi_text);
        airNowMainText = findViewById(R.id.air_now_main_text);
        airNowQltyText = findViewById(R.id.air_now_qlty_text);
        airNowPm10Text = findViewById(R.id.air_now_pm10_text);
        airNowPm25Text = findViewById(R.id.air_now_pm25_text);
        airNowNo2Text = findViewById(R.id.air_now_no2_text);
        airNowSo2Text = findViewById(R.id.air_now_so2_text);
        airNowCoText = findViewById(R.id.air_now_co_text);
        airNowO3Text = findViewById(R.id.air_now_o3_text);
        airNowCard = findViewById(R.id.air_now_card);

        //生活指数view初始化
        lifeStyleBrfText = findViewById(R.id.life_style_brf_text);
        lifeStyleTypeText = findViewById(R.id.life_style_type_text);
        lifeStyleCard = findViewById(R.id.life_style_card);

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
            String windDir = "风向：" + now.getWind_dir();
            String windSpd = now.getWind_spd() + " KM/H";
            String windSc = now.getWind_sc() + "级";
            String condTxt = now.getCond_txt();
            String tmp = now.getTmp() + "°";
            String fl = "体感温度：" + now.getFl() + "°";
            String cloud = now.getCloud();
            int condCode = IconUtils.getDayIconDark(now.getCond_code());
            int backCode = IconUtils.getDayBack(now.getCond_code());
            nowTmpText.setText(tmp);
            nowCondText.setText(condTxt);
            nowFlText.setText(fl);
            nowWindDirText.setText(windDir);
            nowWindSpdText.setText(windSpd);
            nowWindScText.setText(windSc);
            nowHumText.setText(hum);
            nowPcpnText.setText(rain);
            if (rain.equals("0.0mm")) {
                LinearLayout nowPcpnLayout = findViewById(R.id.now_pcpn_layout);
                nowPcpnLayout.setVisibility(View.GONE);
            }
            nowPresText.setText(pres);
            nowVisText.setText(vis);
            nowCloudText.setText(cloud);
            if (cloud.equals("0")) {
                LinearLayout nowCloudLayout = findViewById(R.id.now_cloud_layout);
                nowCloudLayout.setVisibility(View.GONE);
            }
            nowCondIcon.setImageResource(condCode);
            weatherBackground.setBackgroundResource(backCode);
            title.setTitle(city);
            updateTime = TransUnitUtil.getUpdateTime(updateTime);
            title.setSubtitle(updateTime);
        }
    }

    @Override
    public void getWeatherForecast(Forecast bean) {
        if (bean != null && bean.getDaily_forecast() != null) {
            List<ForecastBase> forecastList = bean.getDaily_forecast();
            forecastLayout.removeAllViews();
            int i = 0;
            for (ForecastBase forecast : forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.items_weather_forecast,
                        forecastLayout, false);
                wfCondText = view.findViewById(R.id.wf_cond_text);
                wfDateText = view.findViewById(R.id.wf_date_text);
                wfCondIcon = view.findViewById(R.id.wf_cond_icon);
                wfTmpText = view.findViewById(R.id.wf_tmp_text);
                String time = TransUnitUtil.getForecastTime(forecast.getDate(), i++);
                wfDateText.setText(time);
                wfCondText.setText(forecast.getCond_txt_d());
                int condCode = IconUtils.getDayIconDark(forecast.getCond_code_d());
                wfCondIcon.setImageResource(condCode);
                String wfTmp = forecast.getTmp_max() + " / " + forecast.getTmp_min() + "°";
                wfTmpText.setText(wfTmp);
                forecastLayout.addView(view);
            }
        }
    }

    @Override
    public void getAirNow(AirNow bean) {
        if (bean != null && bean.getAir_now_city() != null) {
            airNowCard.setVisibility(View.VISIBLE);
            AirNowCity airNowCity = bean.getAir_now_city();
            String main = airNowCity.getMain();
            String qlty = airNowCity.getQlty();
            String aqi = airNowCity.getAqi();
            String pm25 = airNowCity.getPm25();
            String pm10 = airNowCity.getPm10();
            String so2 = airNowCity.getSo2();
            String no2 = airNowCity.getNo2();
            String co = airNowCity.getCo();
            String o3 = airNowCity.getO3();
            airNowAqiText.setText(aqi);
            if (main.equals("-")) {
                LinearLayout airNowMainLayout = findViewById(R.id.air_now_main_layout);
                airNowMainLayout.setVisibility(View.GONE);
            } else {
                main = TransUnitUtil.getAirNowMain(main);
            }
            airNowMainText.setText(main);
            airNowQltyText.setText(qlty);
            airNowPm10Text.setText(pm10);
            airNowPm25Text.setText(pm25);
            airNowNo2Text.setText(no2);
            airNowSo2Text.setText(so2);
            airNowCoText.setText(co);
            airNowO3Text.setText(o3);
        }
    }

    @Override
    public void getWeatherHourly(Hourly bean) {
        if (bean != null && bean.getHourly() != null) {
            hourlyCard.setVisibility(View.VISIBLE);
            weatherHourlyBean = bean;
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
            //设置当天的最高最低温度
            hourlyForecastView.setHighestTemp(maxTmp);
            hourlyForecastView.setLowestTemp(minTmp);
            if (maxTmp == minTmp) {
                hourlyForecastView.setLowestTemp(minTmp - 1);
            }
            hourlyForecastView.initData(data);
            tvLineMax.setText(maxTmp + "°");
            tvLineMin.setText(minTmp + "°");
        }
    }

    @Override
    public void getWeatherLifeStyle(Lifestyle bean) {
//        if (bean != null && bean.getLifestyle() != null) {
//            lifeStyleCard.setVisibility(View.VISIBLE);
//            LifestyleBase lifestyleBase = bean.getLifestyle().get(0);
//            String brf = lifestyleBase.getBrf();
//            String txt = lifestyleBase.getTxt();
//            String type = lifestyleBase.getType();
//            lifeStyleBrfText.setText(brf);
//            lifeStyleBrfText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    lifeStyleBrfText.setText(txt);
//                }
//            });
//            lifeStyleTypeText.setText(type);
//        }
    }
}
