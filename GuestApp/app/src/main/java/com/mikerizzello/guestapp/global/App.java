package com.mikerizzello.guestapp.global;

import android.app.Application;
import android.content.Context;


/**
 * Created by michaelrizzello on 2016-12-12.
 */

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getmContext() {
        return mContext;
    }


}
