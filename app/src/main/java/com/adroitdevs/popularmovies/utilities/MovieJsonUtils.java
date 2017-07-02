package com.adroitdevs.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by doxa on 01/07/2017.
 */

public class MovieJsonUtils {
    public static String[][] getSimpleMovieStringFromJson(String movieJsonStr) throws JSONException {
        final String TMDB_RESULT = "results";
        final String TMDB_TITLE = "title";
        final String TMDB_IMAGE = "poster_path";
        final String TMDB_SYNOPSYS = "overview";
        final String TMDB_RATING = "vote_average";
        final String TMDB_RELEASE_DATE = "release_date";

        String[][] parsedMovieData = null;
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULT);
        parsedMovieData = new String[movieArray.length()][5];

        for (int i = 0; i < movieArray.length(); i++) {
            String title, image, synopsis, rating, releaseDate;

            JSONObject movieDetail = movieArray.getJSONObject(i);
            title = movieDetail.getString(TMDB_TITLE);
            image = movieDetail.getString(TMDB_IMAGE);
            synopsis = movieDetail.getString(TMDB_SYNOPSYS);
            rating = movieDetail.getString(TMDB_RATING);
            releaseDate = movieDetail.getString(TMDB_RELEASE_DATE);

            parsedMovieData[i][0] = title;
            parsedMovieData[i][1] = image;
            parsedMovieData[i][2] = synopsis;
            parsedMovieData[i][3] = rating;
            parsedMovieData[i][4] = releaseDate;
        }

        return parsedMovieData;
    }
}
