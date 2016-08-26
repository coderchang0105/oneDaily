package com.example.coderchang.onedaily;

import android.app.Application;
import android.content.Context;

/**
 * Created by coderchang on 16/8/26.
 */
public class MyApplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
