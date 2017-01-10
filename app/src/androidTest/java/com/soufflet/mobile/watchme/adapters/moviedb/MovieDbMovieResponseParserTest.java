package com.soufflet.mobile.watchme.adapters.moviedb;

import android.support.test.espresso.core.deps.guava.io.CharStreams;

import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.adapters.moviedb.parsers.MovieDbMovieResponseParser;
import com.soufflet.mobile.watchme.types.Movie;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class MovieDbMovieResponseParserTest {

    private static final String TEST_FILE_NAME = "movieDb_movieApi_response.json";

    @Test
    public void givenValidJsonObject_whenParseResponse_thenReturnExpectedNumberOfMovies() throws Exception {
        // GIVEN
        JSONObject jsonObject = new JSONObject(getFileAsString(TEST_FILE_NAME));

        // WHEN
        ImmutableList<Movie> movies = MovieDbMovieResponseParser.parsePopularMovies(jsonObject);

        // THEN
        assertEquals(movies.size(), 20);

        assertEquals(movies.get(12).title(), "Terminator Genisys");
    }

    private String getFileAsString(String fileName) throws IOException {
        return CharStreams.toString(
                new InputStreamReader(
                        getInstrumentation().getContext().getAssets().open(fileName),
                        UTF_8));
    }

}