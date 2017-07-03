package com.adroitdevs.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitdevs.popularmovies.utilities.MovieJsonUtils;
import com.adroitdevs.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String POPULAR_TMDB = "popular";
    private static final String TOP_RATED_TMDB = "top_rated";
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String listPreference = POPULAR_TMDB;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_popular_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        loadMovieData(listPreference);
    }

    private void loadMovieData(String preference) {
        showMovieDataView();
        new FetchMovieTask().execute(preference);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by_popular) {
            setTitle("Popular Movies");
            mMovieAdapter.setMovieData(null);
            listPreference = POPULAR_TMDB;
            loadMovieData(listPreference);
            return true;
        }
        if (id == R.id.sort_by_highest_rated) {
            setTitle("Highest Rated Movies");
            mMovieAdapter.setMovieData(null);
            listPreference = TOP_RATED_TMDB;
            loadMovieData(listPreference);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Map<String, String> infoDetail) {
        if (toast != null) {
            toast.cancel();
        } else {
            toast.makeText(this, infoDetail.get("title"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DetailActivity.class);
            String[] movieDetail = new String[5];
            movieDetail[0] = infoDetail.get("title");
            movieDetail[1] = infoDetail.get("image");
            movieDetail[2] = infoDetail.get("synopsis");
            movieDetail[3] = infoDetail.get("rating");
            movieDetail[4] = infoDetail.get("releaseDate");
            intent.putExtra("detail", movieDetail);
            startActivity(intent);
        }

    }

    private class FetchMovieTask extends AsyncTask<String, Void, String[][]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {
            if (params.length == 0)
                return null;

            String preferenceSetting = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(preferenceSetting);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                return MovieJsonUtils
                        .getSimpleMovieStringFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }
}
