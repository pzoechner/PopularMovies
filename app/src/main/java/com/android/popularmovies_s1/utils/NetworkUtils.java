package com.android.popularmovies_s1.utils;

import android.net.Uri;

import com.android.popularmovies_s1.PopularMoviesApplication;
import com.android.popularmovies_s1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Philipp Zoechner
 * @date 07/08/2017
 */
public class NetworkUtils {

    // TODO note in README to replace this
    private static String API_KEY = PopularMoviesApplication.getAppContext().getString(R.string.MOVIEDB_API_KEY);
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    private final static String PARAM_API_KEY = "api_key";


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
