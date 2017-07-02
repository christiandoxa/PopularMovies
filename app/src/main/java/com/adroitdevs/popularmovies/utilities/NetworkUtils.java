package com.adroitdevs.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by doxa on 01/07/2017.
 */

public class NetworkUtils {
    private final static String TAG = NetworkUtils.class.getSimpleName();
    private final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String API_KEY = "58524a76666af7251dfda9c37f7e940b";
    private final static String PAGE = "1";
    private final static String API_KEY_PARAM = "api_key";
    private final static String PAGE_PARAM = "page";

    public static URL buildUrl(String preferenceSetting) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(preferenceSetting)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, PAGE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI" + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
