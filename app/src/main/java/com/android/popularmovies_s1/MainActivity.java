package com.android.popularmovies_s1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies_s1.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * @author Philipp Zoechner
 * @date 07.08.2017
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private MoviesAdapter mAdapter;
    private RecyclerView rvMovies;

    private ProgressBar pbLoading;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = (RecyclerView) findViewById(R.id.rv_main_movies);

        pbLoading = (ProgressBar) findViewById(R.id.pb_main_loading);
        tvError = (TextView) findViewById(R.id.tv_main_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setHasFixedSize(true);

        rvMovies.setAdapter(mAdapter);


        listPopular();
    }

    private void listPopular() {
        String type = NetworkUtils.POPULAR;
        new MovieQueryTask().execute(type);
    }

    private void showMovies() {
        pbLoading.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void hideMovies() {
        rvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }


    private class MovieQueryTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            hideMovies();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String type = params[0];
            URL url = NetworkUtils.buildUrl(type);

            JSONObject result = new JSONObject();
            try {
                result = new JSONObject(NetworkUtils.getResponseFromHttpUrl(url));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                mAdapter = new MoviesAdapter(result.getJSONArray("results"));
                rvMovies.setAdapter(mAdapter);
                showMovies();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
