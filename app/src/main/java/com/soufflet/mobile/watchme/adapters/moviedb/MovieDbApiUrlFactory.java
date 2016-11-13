package com.soufflet.mobile.watchme.adapters.moviedb;

import android.net.Uri;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi.POPULAR_MOVIES;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi.TOP_RATED_MOVIES;

public final class MovieDbApiUrlFactory {

    public enum MovieDbApi {
        POPULAR_MOVIES,
        TOP_RATED_MOVIES
    }

    /**
     * @see https://www.themoviedb.org/
     */
    private static final String API_KEY = "[YOUR_API_KEY]";

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/";
    private static final String MOVIE_API = "movie";
    private static final String POPULAR_MOVIES_PATH = "popular";
    private static final String TOP_RATED_MOVIES_PATH = "top_rated";
    private static final String APPID_PARAM = "api_key";

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMG_SIZE = "w185";

    private static final Map<MovieDbApi, Uri> movieDbURIs =
            ImmutableMap.<MovieDbApi, Uri>builder()
                    .put(POPULAR_MOVIES, createPopularMoviesEndpoint())
                    .put(TOP_RATED_MOVIES, createTopRatedMoviesEndpoint())
                    .build();

    private MovieDbApiUrlFactory() {}

    public static Uri getMovieDbEndpoint(MovieDbApi api) {
        return movieDbURIs.get(api);
    }

    public static Uri createMovieDbImageUriFor(String imagePath) {
        return Uri.parse(IMG_BASE_URL).buildUpon()
                .appendEncodedPath(IMG_SIZE + "/" + imagePath)
                .build();
    }

    private static Uri createPopularMoviesEndpoint() {
        return Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_API + "/" + POPULAR_MOVIES_PATH)
                .appendQueryParameter(APPID_PARAM, API_KEY)
                .build();
    }

    private static Uri createTopRatedMoviesEndpoint() {
        return Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_API + "/" + TOP_RATED_MOVIES_PATH)
                .appendQueryParameter(APPID_PARAM, API_KEY)
                .build();
    }
}
