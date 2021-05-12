package com.truevisionsa;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        ;
    }
}