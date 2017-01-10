package com.soufflet.mobile.watchme.adapters.moviedb.parsers;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.types.YoutubeTrailer;

import org.json.JSONArray;
import org.json.JSONObject;

public final class MovieDbTrailerResponseParser {

    private static final String LOG_TAG = MovieDbTrailerResponseParser.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String SITE = "site";
    private static final String TYPE = "type";

    private MovieDbTrailerResponseParser() {}

    public static ImmutableList<YoutubeTrailer> parseTrailers(JSONObject jsonObject) {
        try {
            ImmutableList.Builder<YoutubeTrailer> trailers = ImmutableList.builder();
            JSONArray trailersJsonArray = jsonObject.getJSONArray(RESULTS);

            for (int i = 0; i < trailersJsonArray.length(); i++) {
                JSONObject trailerJson = trailersJsonArray.getJSONObject(i);

                /*
                 * The App only supports Youtube trailer for now.
                 * Filtering out the potential other content.
                 */
                if (trailerJson.getString(SITE).equalsIgnoreCase("YouTube")
                        && trailerJson.getString(TYPE).equalsIgnoreCase("Trailer")) {
                    trailers.add(
                            YoutubeTrailer.create(
                                    trailerJson.getString(ID),
                                    trailerJson.getString(NAME),
                                    trailerJson.getString(KEY)));
                }
            }

            return trailers.build();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            Log.e(LOG_TAG, jsonObject.toString());
            throw new RuntimeException(e);
        }
    }
}
