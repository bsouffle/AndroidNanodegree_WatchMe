package com.soufflet.mobile.watchme.types;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soufflet.mobile.watchme.utils.UriSerializer;

public final class MovieMapper {

    public static String MOVIE_JSON_EXTRA = "movie_json";
    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();

    private MovieMapper() {}

    public static Movie parseJSON(String jsonString) {
        return gson.fromJson(jsonString, Movie.class);
    }

    public static String toJson(Movie movie) {
        return gson.toJson(movie);
    }
}
