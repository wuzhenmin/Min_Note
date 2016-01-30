package com.example.zhenmin.min_note.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
