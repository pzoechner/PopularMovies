package com.android.popularmovies_s1;

import android.app.Application;
import android.content.Context;

/**
 * @author Philipp Zoechner
 * @date 07/08/2017
 */
public class PopularMoviesApplication extends Application {

    static private Context mContext;

    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return PopularMoviesApplication.mContext;
    }


}
