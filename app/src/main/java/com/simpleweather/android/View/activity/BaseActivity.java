package com.simpleweather.android.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import com.simpleweather.android.util.LanguageUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LanguageUtil.attachBaseContext(newBase);
        super.attachBaseContext(context);
    }

}
