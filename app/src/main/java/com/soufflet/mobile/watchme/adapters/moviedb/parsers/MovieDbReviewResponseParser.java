package com.soufflet.mobile.watchme.adapters.moviedb.parsers;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.types.Review;

import org.json.JSONArray;
import org.json.JSONObject;

public final class MovieDbReviewResponseParser {

    private static final String LOG_TAG = MovieDbReviewResponseParser.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private MovieDbReviewResponseParser() {}

    public static ImmutableList<Review> parseReviews(JSONObject jsonObject) {
        try {
            ImmutableList.Builder<Review> reviews = ImmutableList.builder();
            JSONArray reviewsJsonArray = jsonObject.getJSONArray(RESULTS);

            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

                reviews.add(
                        Review.create(
                                reviewJson.getString(ID),
                                reviewJson.getString(AUTHOR),
                                reviewJson.getString(CONTENT)));
            }

            return reviews.build();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
