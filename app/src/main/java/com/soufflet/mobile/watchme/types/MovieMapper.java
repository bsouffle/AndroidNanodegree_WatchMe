package com.soufflet.mobile.watchme.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class MovieMapper {

    public static final String MOVIE_JSON_EXTRA = "movie_json";

    private static final Gson gson = new GsonBuilder().create();

    private MovieMapper() {}

    public static Movie parseJSON(String jsonString) {
        return gson.fromJson(jsonString, Movie.class);
    }

    public static String toJson(Movie movie) {
        return gson.toJson(movie);
    }
}
