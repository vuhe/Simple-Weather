package com.simpleweather.android.View.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.simpleweather.android.R;
import com.simpleweather.android.adapter.SearchAdapter;
import com.simpleweather.android.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private RecyclerView rvSearch;
    private Lang lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //测试时，仅设置中文
        lang = Lang.CHINESE_SIMPLIFIED;
        initView();
        initSearch();
    }

    private void initView() {
        ImageView ivBack = findViewById(R.id.search_back);
        etSearch = findViewById(R.id.et_search);
        rvSearch = findViewById(R.id.recycle_search);
        LinearLayoutManager sevenManager = new LinearLayoutManager(this);
        sevenManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(sevenManager);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSearch() {
        etSearch.setThreshold(1);
        //编辑框输入监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchResult = etSearch.getText().toString();
                if (!TextUtils.isEmpty(searchResult)) {
                    rvSearch.setVisibility(View.VISIBLE);
                    getSearchResult(searchResult);
                } else {
                    rvSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getSearchResult(String location) {
        HeWeather.getSearch(this, location, "cn,overseas", 10, lang,
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
                                        SearchActivity.this, data,
                                        etSearch.getText().toString());
                                rvSearch.setAdapter(searchAdapter);
                                searchAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
