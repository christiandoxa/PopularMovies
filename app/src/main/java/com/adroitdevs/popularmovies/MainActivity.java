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
    private final int DEFAULT_PAGE = 1;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String listPreference = POPULAR_TMDB;
    private int page = DEFAULT_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("page") | getIntent().hasExtra("preference")) {
            String preference = getIntent().getStringExtra("preference");
            page = Integer.parseInt(getIntent().getStringExtra("page"));
            listPreference = preference;
        }


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
        new FetchMovieTask().execute(preference, String.valueOf(page));
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
            page = 1;
            loadMovieData(listPreference);
            return true;
        }
        if (id == R.id.sort_by_highest_rated) {
            setTitle("Highest Rated Movies");
            mMovieAdapter.setMovieData(null);
            listPreference = TOP_RATED_TMDB;
            page = 1;
            loadMovieData(listPreference);
            return true;
        }
        if (id == R.id.next) {
            CharSequence title = getTitle();
            setTitle(title);
            mMovieAdapter.setMovieData(null);
            page += 1;
            loadMovieData(listPreference);
            return true;
        }
        if (id == R.id.previous) {
            if (page > 1) {
                CharSequence title = getTitle();
                setTitle(title);
                mMovieAdapter.setMovieData(null);
                page -= 1;
                loadMovieData(listPreference);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Map<String, String> infoDetail) {
        Toast.makeText(this, infoDetail.get("title"), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);
        String[] movieDetail = new String[7];
        movieDetail[0] = infoDetail.get("title");
        movieDetail[1] = infoDetail.get("image");
        movieDetail[2] = infoDetail.get("synopsis");
        movieDetail[3] = infoDetail.get("rating");
        movieDetail[4] = infoDetail.get("releaseDate");
        movieDetail[5] = listPreference;
        movieDetail[6] = String.valueOf(page);

        intent.putExtra("detail", movieDetail);
        startActivity(intent);
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
            String page = params[1];

            URL movieRequestUrl = NetworkUtils.buildUrl(preferenceSetting, page);

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