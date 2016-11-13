package com.soufflet.mobile.watchme.types;

import android.net.Uri;

import java.net.URL;
import java.util.Date;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Movie {

    private final String title;
    private final Uri imageUri;
    private final String overview;
    private final double rating;
    private final Date releaseDate;

    public Movie(String title, Uri imageUri, String overview, double rating, Date releaseDate) {
        this.title = title;
        this.imageUri = imageUri;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }
}
