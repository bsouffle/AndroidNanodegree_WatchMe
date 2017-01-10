package com.soufflet.mobile.watchme.dao;

import com.soufflet.mobile.watchme.types.Movie;

public final class InternalMovieTypeConverter {

    private InternalMovieTypeConverter() {}

    public static Movie toMovie(InternalMovie internalMovie) {
        return Movie.create(
                internalMovie.movie_id,
                internalMovie.title,
                internalMovie.poster,
                internalMovie.description,
                internalMovie.vote_average,
                internalMovie.release_date);
    }

    public static InternalMovie fromMovie(Movie movie) {
        InternalMovie internalMovie = new InternalMovie();
        internalMovie.movie_id = movie.id();
        internalMovie.description = movie.overview();
        internalMovie.poster = movie.imagePath();
        internalMovie.release_date = movie.releaseDate();
        internalMovie.title = movie.title();
        internalMovie.vote_average = movie.rating();

        return internalMovie;
    }
}
