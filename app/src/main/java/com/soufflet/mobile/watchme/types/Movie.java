package com.soufflet.mobile.watchme.types;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Movie implements Parcelable {

    public static final String MOVIE_EXTRA_PARCEL = "movie_parcel";

    public enum MovieDataSources {
        THE_MOVIE_DB_POPULAR_MOVIES,
        THE_MOVIE_DB_TOP_RATED_MOVIES,
        INTERNAL_MOVIE_STORE_FAVORITES
    }

    public static Movie create(
            long id, String title, String imagePath, String overview, double rating, Date releaseDate) {
        return new AutoValue_Movie(id, title, imagePath, overview, rating, releaseDate);
    }

    public abstract long id();
    public abstract String title();
    public abstract String imagePath();
    public abstract String overview();
    public abstract double rating();
    public abstract Date releaseDate();
}
