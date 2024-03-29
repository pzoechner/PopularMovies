package com.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Philipp Zoechner
 * @date 07.08.2017
 */
class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static String TAG = MainActivity.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    private JSONArray mMovieResults;
    private int mNumberOfItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    MoviesAdapter(JSONArray results, ListItemClickListener listener) {
        mMovieResults = results;
        mOnClickListener = listener;
        mNumberOfItems = results.length();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_movie_list, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_movie_list_poster);

            ivPoster.setOnClickListener(this);
        }

        void bind(int listIndex) {
            try {
                JSONObject item = new JSONObject(String.valueOf(mMovieResults.getJSONObject(listIndex)));

                String posterUrl = NetworkUtils.getPosterUrl(item, NetworkUtils.w342);
                Picasso.with(itemView.getContext())
                        .load(posterUrl)
                        .into(ivPoster);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
