package com.android.popularmovies_s1;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmovies_s1.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {


    private TextView tvTitle, tvReleaseDate, tvOverview, tvRating;
    private ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = (TextView) findViewById(R.id.tv_detail_title);
        ivPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        tvReleaseDate = (TextView) findViewById(R.id.tv_detail_year_release);
        tvOverview = (TextView) findViewById(R.id.tv_detail_overview);
        tvRating = (TextView) findViewById(R.id.tv_detail_score);


        if (getIntent() != null) {
            if (getIntent().hasExtra(MainActivity.EXTRA_MOVIE_ITEM)) {

                try {
                    JSONObject movie = new JSONObject(getIntent().getStringExtra(MainActivity.EXTRA_MOVIE_ITEM));

                    showData(movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    private void showData(JSONObject movie) throws JSONException {
        tvTitle.setText(movie.getString("original_title"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTitle.setElevation(25);
        }

        Picasso.with(this)
                .load(NetworkUtils.getPosterUrl(movie, NetworkUtils.w342))
                .into(ivPoster);
        tvReleaseDate.setText((movie.getString("release_date")).substring(0,4));
        tvOverview.setText(movie.getString("overview"));
        tvRating.setText(getString(R.string.movie_vote_average, movie.getString("vote_average")));
    }


}
