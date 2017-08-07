package com.android.popularmovies_s1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies_s1.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private GridView gvMovies;

    private ProgressBar pbLoading;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvMovies = (GridView) findViewById(R.id.gv_main_movies);

        pbLoading = (ProgressBar) findViewById(R.id.pb_main_loading);
        tvError = (TextView) findViewById(R.id.tv_main_error_message_display);

        listPopular();
    }

    private void listPopular() {
        String type = NetworkUtils.POPULAR;
        new MovieQueryTask().execute(type);
    }

    private void showMovies() {
        pbLoading.setVisibility(View.INVISIBLE);
        gvMovies.setVisibility(View.VISIBLE);
    }

    private void hideMovies() {
        gvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }


    private class MovieQueryTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            hideMovies();
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            URL url = NetworkUtils.buildUrl(type);

            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);

                Log.d("MainActivity-async", result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            showMovies();
        }
    }

}
