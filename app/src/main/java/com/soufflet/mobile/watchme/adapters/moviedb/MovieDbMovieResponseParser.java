package com.soufflet.mobile.watchme.adapters.moviedb;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.types.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createMovieDbImageUriFor;

public final class MovieDbMovieResponseParser {

    private static final String LOG_TAG = MovieDbMovieResponseParser.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String IMAGE_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String DATE = "release_date";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private MovieDbMovieResponseParser() {}

    public static ImmutableList<Movie> parseMovieDbResponse(JSONObject jsonObject) {
        try {
            ImmutableList.Builder<Movie> movies = ImmutableList.builder();
            JSONArray moviesJsonArray = jsonObject.getJSONArray(RESULTS);

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject movieJson = moviesJsonArray.getJSONObject(i);

                movies.add(
                        new Movie(
                                movieJson.getString(TITLE),
                                movieJson.getString(IMAGE_PATH),
                                movieJson.getString(OVERVIEW),
                                movieJson.getDouble(RATING),
                                DATE_FORMAT.parse(movieJson.getString(DATE))));
            }

            return movies.build();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
