package com.simpleweather.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import java.util.Locale;

import interfaces.heweather.com.interfacesmodule.bean.Lang;

public class LanguageUtil {

    public static Context attachBaseContext(Context context) {
        Resources resources = context.getResources();
        Locale locale = getLocale();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    private static Locale getLocale() {
        if (ContentUtil.lang == Lang.ENGLISH) {
            return Locale.ENGLISH;
        } else if (ContentUtil.lang == Lang.CHINESE_SIMPLIFIED) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

}
