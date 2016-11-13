package com.soufflet.mobile.watchme.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.adapters.HttpRequestManager;
import com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi;
import com.soufflet.mobile.watchme.detailpage.MovieOverviewActivity;
import com.soufflet.mobile.watchme.types.Movie;

import org.json.JSONObject;
import org.parceler.Parcels;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.Request.Method.GET;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi.POPULAR_MOVIES;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi.TOP_RATED_MOVIES;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.getMovieDbEndpoint;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbMovieResponseParser.parseMovieDbResponse;
import static com.soufflet.mobile.watchme.types.Movie.MOVIE_EXTRA_PARCEL;

/**
 * - Fetch Popular and Top Rated movies.
 * - Populates the grid view containing the movie posters.
 * - Handles interactions with the detail page. (sends intent with movie data in JSON format).
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private MovieGridAdapter imageGridAdapter;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.movies_gridview);

        imageGridAdapter =
                new MovieGridAdapter(
                        getContext(),
                        R.layout.fragment_main,
                        R.layout.movie_grid_item);

        gridview.setAdapter(imageGridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                gotoDetailPage(imageGridAdapter.getItem(position));
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchAndDisplayMoviesData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                registerSortPreference(POPULAR_MOVIES);
                fetchAndDisplayMoviesData();
                break;
            case R.id.sort_by_ratings:
                registerSortPreference(TOP_RATED_MOVIES);
                fetchAndDisplayMoviesData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoDetailPage(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieOverviewActivity.class);
        intent.putExtra(MOVIE_EXTRA_PARCEL, Parcels.wrap(movie));

        startActivity(intent);
    }

    private void fetchAndDisplayMoviesData() {
        JsonObjectRequest request = new JsonObjectRequest
                (GET, getMovieDbEndpoint(getMovieDbApiFromPreferences()).toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ImmutableList<Movie> movies = parseMovieDbResponse(response);
                        if (!movies.isEmpty()) {
                            imageGridAdapter.clear();
                            imageGridAdapter.addAll(movies);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Error details: " + error.getMessage());

                        final String errorMessage;
                        if (error instanceof NoConnectionError) {
                            errorMessage = "No internet found. Please check your connection and try again.";
                        } else {
                            errorMessage = "Unexpected error. We were unable to fetch movies data.";
                        }

                        Toast.makeText(getContext(), errorMessage, LENGTH_SHORT).show();
                    }
                });

        HttpRequestManager.getInstance(getContext()).addToRequestQueue(request);
    }

    private void registerSortPreference(MovieDbApi api) {
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_sort_pref), api.name());
        editor.apply();
    }

    private MovieDbApi getMovieDbApiFromPreferences() {
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        String registeredApi = sharedPref.getString(getString(R.string.saved_sort_pref), POPULAR_MOVIES.name());

        return MovieDbApi.valueOf(registeredApi);
    }
}
