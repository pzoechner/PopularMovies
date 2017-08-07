package com.android.popularmovies;

import android.app.Application;
import android.content.Context;

/**
 * @author Philipp Zoechner
 * @date 07.08.2017
 */
public class PopularMoviesApplication extends Application {

    // This is used to retrieve API string in static class NetworkUtils
    static private Context mContext;

    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return PopularMoviesApplication.mContext;
    }


}
