package com.android.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.StringDef;

import com.android.popularmovies.PopularMoviesApplication;
import com.android.popularmovies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Philipp Zoechner
 * @date 07.08.2017
 */
public class NetworkUtils {

    private final static String API_KEY = PopularMoviesApplication.getAppContext().getString(R.string.MOVIEDB_API_KEY);
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    private final static String PARAM_API_KEY = "api_key";


    public final static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    @StringDef({
            w92, w154, w185, w342, w500, w780, original
    })
    public @interface IMG_SIZES {
    }

    public static final String w92 = "w92";
    public static final String w154 = "w154";
    public static final String w185 = "w185";
    public static final String w342 = "w342";
    public static final String w500 = "w500";
    public static final String w780 = "w780";
    public static final String original = "original";


    public static URL buildUrl(String type) {
        if (!type.equals(POPULAR) && !type.equals(TOP_RATED)) return null;

        Uri builtUri = Uri.parse(BASE_URL + type).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getPosterUrl(JSONObject movieObject, @IMG_SIZES String size) throws JSONException {
        return IMG_BASE_URL + size + "/" + movieObject.getString("poster_path");
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
