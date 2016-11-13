package com.soufflet.mobile.watchme.types;

import org.parceler.Parcel;

import java.util.Date;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Parcel
public class Movie {

    public static final String MOVIE_EXTRA_PARCEL = "movie_parcel";

    private String title;
    private String imagePath;
    private String overview;
    private double rating;
    private Date releaseDate;

    public Movie() {}

    public Movie(String title, String imagePath, String overview, double rating, Date releaseDate) {
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
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
