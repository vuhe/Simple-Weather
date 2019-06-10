package com.simpleweather.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.simpleweather.android.R;
import com.simpleweather.android.util.ContentUtil;
import com.simpleweather.android.util.SpUtils;

import interfaces.heweather.com.interfacesmodule.bean.Lang;

public class SettingActivity extends BaseActivity {

    private Toolbar settingToolbar;
    private AppCompatSpinner settingLanguage;
    private AppCompatSpinner settingTmpUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        setSpinner();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onDestroy();
    }

    /**
     * 初始化view
     */
    private void initView() {
        settingToolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(settingToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        settingLanguage = findViewById(R.id.setting_language);
        settingLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] language = getResources().getStringArray(R.array.language);
                if (language[position].equals(getResources().getString(R.string.chinese))) {
                    ContentUtil.lang = Lang.CHINESE_SIMPLIFIED;
                    SpUtils.putInt("language", position);
                } else if (language[position].equals(getResources().getString(R.string.english))) {
                    ContentUtil.lang = Lang.ENGLISH;
                    SpUtils.putInt("language", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        settingTmpUnit = findViewById(R.id.setting_tmp_unit);
        settingTmpUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] tmpUnit = getResources().getStringArray(R.array.tmp_unit);
                if (tmpUnit[position].equals(getResources().getString(R.string.she))) {
                    ContentUtil.DEGREE = "C";
                    SpUtils.putInt("tmp_unit", position);
                } else if (tmpUnit[position].equals(getResources().getString(R.string.hua))) {
                    ContentUtil.DEGREE = "F";
                    SpUtils.putInt("tmp_unit", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
     * 设置初始索引
     */
    private void setSpinner() {
        if (SpUtils.getInt("language", -1) != -1) {
            settingLanguage.setSelection(SpUtils.getInt("language"));
        }
        if (SpUtils.getInt("tmp_unit", -1) != -1) {
            settingTmpUnit.setSelection(SpUtils.getInt("tmp_unit"));
        }
    }

}
