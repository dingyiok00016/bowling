package com.cloudysea.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.SharedPreferencesUtils;

import java.util.Locale;

import static com.cloudysea.BowlingApplication.getContext;
import static com.cloudysea.utils.SharedPreferencesUtils.LANGUAGE;

/**
 * @author roof 2019/12/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class BaseLanguageActivity extends FragmentActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT < 26) {
            super.attachBaseContext(newBase);
        } else {
            //zh：中文
            int language = (int) SharedPreferencesUtils.getParam(LANGUAGE, 1);
            String lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            if (language == 1) {
                lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            } else if (language == 2) {
                lang = Locale.ENGLISH.getLanguage();
            } else if (language == 3) {
                lang = Locale.KOREAN.getLanguage();
            }
            super.attachBaseContext(BowlingUtils.initAppLanguage(newBase, lang));
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = BowlingUtils.getLanuage();
        resources.updateConfiguration(config, dm);
        return resources;
    }
}
