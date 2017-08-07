package com.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * @author Philipp Zoechner
 * @date 07.08.2017
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private MoviesAdapter mAdapter;
    private RecyclerView rvMovies;

    private ProgressBar pbLoading;
    private TextView tvError;

    private JSONArray mMovies;
    public static final String EXTRA_MOVIE_ITEM = "extra_movie_item";

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
        tvError.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void hideMovies(boolean error) {
        rvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);

        if (error) {
            pbLoading.setVisibility(View.INVISIBLE);
            tvError.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                new MovieQueryTask().execute(NetworkUtils.POPULAR);
                return true;

            case R.id.action_sort_top_rated:
                new MovieQueryTask().execute(NetworkUtils.TOP_RATED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        JSONObject movie;
        try {
            movie = (JSONObject) mMovies.get(clickedItemIndex);

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(EXTRA_MOVIE_ITEM, movie.toString());
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * from: https://stackoverflow.com/a/4009133/1163881
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class MovieQueryTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            hideMovies(false);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String type = params[0];
            URL url = NetworkUtils.buildUrl(type);

            JSONObject result = new JSONObject();
            try {
                if (isOnline()) {
                    result = new JSONObject(NetworkUtils.getResponseFromHttpUrl(url));
                } else {
                    hideMovies(true);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                hideMovies(true);
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                mMovies = result.getJSONArray("results");
                mAdapter = new MoviesAdapter(mMovies, MainActivity.this);
                rvMovies.setAdapter(mAdapter);
                showMovies();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
