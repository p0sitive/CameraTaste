package com.lee.cameratest;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by lihe6 on 2016/8/11.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NettoOT.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}
