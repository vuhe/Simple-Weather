package com.simpleweather.android.view.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.simpleweather.android.MyApplication;
import com.simpleweather.android.R;
import com.simpleweather.android.adapter.SearchAdapter;
import com.simpleweather.android.bean.CityBean;
import com.simpleweather.android.util.ContentUtil;
import com.simpleweather.android.util.SpUtils;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static com.simpleweather.android.util.ContentUtil.LOCATION_ERROR;
import static com.simpleweather.android.util.ContentUtil.NO_LOCATION_PERMISSION;
import static com.simpleweather.android.util.ContentUtil.POSITIONING;
import static com.simpleweather.android.util.ContentUtil.L_SUCCESS;
import static com.simpleweather.android.util.ContentUtil.lang;

public class SearchActivity extends BaseActivity {

    private Toolbar searchToolbar;
    private SearchView searchView;
    private RecyclerView rvSearch;
    private TextView searchLocation;
    private ImageView searchRefresh;
    private Context context = MyApplication.getContext();
    private String locationCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new LocationTask().execute();
        initView();
        initSearch();
    }

    /**
     * 初始化view
     */
    private void initView() {
        searchToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        searchView = findViewById(R.id.search_view);
        rvSearch = findViewById(R.id.recycle_search);
        LinearLayoutManager sevenManager = new LinearLayoutManager(this);
        sevenManager.setOrientation(RecyclerView.VERTICAL);
        rvSearch.setLayoutManager(sevenManager);
        searchRefresh = findViewById(R.id.search_refresh);
        searchRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LocationTask().execute();
                Toast.makeText(context, "已更新位置", Toast.LENGTH_SHORT).show();
            }
        });
        searchLocation = findViewById(R.id.search_location);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ContentUtil.LOCATE_STATUS) {
                    case NO_LOCATION_PERMISSION:
                        break;
                    case POSITIONING:
                        Toast.makeText(context, "定位中，请稍后", Toast.LENGTH_SHORT).show();
                        break;
                    case LOCATION_ERROR:
                        Toast.makeText(context, "无法定位，请检查网络或定位设置", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case L_SUCCESS:
                        if (locationCid != null) {
                            SpUtils.putString("cityId", locationCid);
                        } else {
                            Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                }
            }
        });
    }

    private void initSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {
                    rvSearch.setVisibility(View.VISIBLE);
                    getSearchResult(s);
                } else {
                    rvSearch.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取城市信息
     */
    private void getSearchResult(String location) {
        HeWeather.getSearch(context, location, "cn,overseas", 10, lang,
                new HeWeather.OnResultSearchBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Search search = new Search();
                        search.setStatus("noData");
                    }

                    @Override
                    public void onSuccess(Search search) {
                        if (!search.getStatus().equals("unknown city")
                                && !search.getStatus().equals("noData")) {
                            final List<Basic> basic = search.getBasic();
                            List<CityBean> data = new ArrayList<>();

                            if (basic != null && basic.size() > 0) {
                                if (data.size() > 0) {
                                    data.clear();
                                }
                                for (int i = 0; i < basic.size(); i++) {
                                    Basic basicData = basic.get(i);
                                    String parentCity = basicData.getParent_city();
                                    String adminArea = basicData.getAdmin_area();
                                    String cnty = basicData.getCnty();
                                    if (TextUtils.isEmpty(parentCity)) {
                                        parentCity = adminArea;
                                    }
                                    if (TextUtils.isEmpty(adminArea)) {
                                        parentCity = cnty;
                                    }
                                    CityBean cityBean = new CityBean();
                                    cityBean.setCityName(parentCity + " - " + basicData
                                            .getLocation());
                                    cityBean.setCityId(basicData.getCid());
                                    cityBean.setCnty(cnty);
                                    cityBean.setAdminArea(adminArea);
                                    data.add(cityBean);
                                }

                                SearchAdapter searchAdapter = new SearchAdapter(
                                        SearchActivity.this, data, location);
                                rvSearch.setAdapter(searchAdapter);
                                searchAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    class LocationTask extends AsyncTask<Void, Void, Boolean> {

        private String cityName;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (ContentUtil.LOCATE_STATUS == L_SUCCESS) {
                setLocation();
            } else if (ContentUtil.LOCATE_STATUS == POSITIONING) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setLocation();
                            }
                        });
                    }
                }).start();
            } else {
                publishProgress();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            switch (ContentUtil.LOCATE_STATUS) {
                case L_SUCCESS:
                    searchLocation.setText(cityName);
                    break;
                case LOCATION_ERROR:
                    searchLocation.setText(R.string.location_error);
                    break;
                case POSITIONING:
                    searchLocation.setText(R.string.positioning);
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }

        private void setLocation() {
            String lotlat = ContentUtil.NOW_LON + "," + ContentUtil.NOW_LAT;
            HeWeather.getSearch(context, lotlat, "cn,overseas", 10, lang,
                    new HeWeather.OnResultSearchBeansListener() {
                        @Override
                        public void onError(Throwable throwable) {
                            ContentUtil.LOCATE_STATUS = LOCATION_ERROR;
                            publishProgress();
                        }

                        @Override
                        public void onSuccess(Search search) {
                            if (search.getStatus().equals("ok")) {
                                final Basic basic = search.getBasic().get(0);
                                locationCid = basic.getCid();
                                String location = basic.getLocation();
                                String parentCity = basic.getParent_city();
                                String adminArea = basic.getAdmin_area();
                                String cnty = basic.getCnty();
                                if (TextUtils.isEmpty(parentCity)) {
                                    parentCity = adminArea;
                                }
                                if (TextUtils.isEmpty(adminArea)) {
                                    parentCity = cnty;
                                }
                                cityName = location + "，" + parentCity + "，" + adminArea + "，"
                                        + cnty;
                                if (TextUtils.isEmpty(adminArea)) {
                                    cityName = location + "，" + parentCity + "，" + cnty;
                                }
                                ContentUtil.CITY_NAME = cityName;
                                if (!TextUtils.isEmpty(cityName)) {
                                    ContentUtil.LOCATE_STATUS = L_SUCCESS;
                                    publishProgress();
                                } else {
                                    ContentUtil.LOCATE_STATUS = LOCATION_ERROR;
                                    publishProgress();
                                }
                            }
                        }
                    });
        }

    }

}
