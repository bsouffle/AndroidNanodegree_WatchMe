package com.soufflet.mobile.watchme.homepage;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.adapters.HttpRequestManager;
import com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbApi;
import com.soufflet.mobile.watchme.dao.FavoriteMoviesTable;
import com.soufflet.mobile.watchme.dao.InternalMovie;
import com.soufflet.mobile.watchme.dao.InternalMovieTypeConverter;
import com.soufflet.mobile.watchme.types.Movie;
import com.soufflet.mobile.watchme.types.Movie.MovieDataSources;
import com.soufflet.mobile.watchme.utils.HttpRequestErrorResponseHandler;
import com.soufflet.mobile.watchme.views.adapters.MoviesRecyclerViewAdapter;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;
import static com.google.common.collect.Lists.newArrayList;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.getMovieDbEndpoint;
import static com.soufflet.mobile.watchme.adapters.moviedb.parsers.MovieDbMovieResponseParser.parsePopularMovies;
import static com.soufflet.mobile.watchme.types.Movie.MovieDataSources.INTERNAL_MOVIE_STORE_FAVORITES;
import static com.soufflet.mobile.watchme.types.Movie.MovieDataSources.THE_MOVIE_DB_POPULAR_MOVIES;
import static com.soufflet.mobile.watchme.types.Movie.MovieDataSources.THE_MOVIE_DB_TOP_RATED_MOVIES;

public class MovieListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.movies_gridview) RecyclerView moviesRecyclerView;

    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    private final List<Movie> movies = newArrayList();

    public MovieListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        moviesRecyclerViewAdapter =
                new MoviesRecyclerViewAdapter(
                        getActivity(),
                        getContext(),
                        getActivity().getSupportFragmentManager(),
                        movies);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
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
                registerSortPreference(THE_MOVIE_DB_POPULAR_MOVIES);
                break;
            case R.id.sort_by_ratings:
                registerSortPreference(THE_MOVIE_DB_TOP_RATED_MOVIES);
                break;
            case R.id.sort_by_favorites:
                registerSortPreference(INTERNAL_MOVIE_STORE_FAVORITES);
                break;
        }

        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
            case R.id.sort_by_ratings:
            case R.id.sort_by_favorites:
                fetchAndDisplayMoviesData();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        fetchAndDisplayMoviesData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void fetchAndDisplayMoviesData() {
        switch (getPreferredMovieDataSource()) {
            case THE_MOVIE_DB_POPULAR_MOVIES:
                retrieveDataFromTheMovieDb(MovieDbApi.POPULAR_MOVIES);
                break;
            case THE_MOVIE_DB_TOP_RATED_MOVIES:
                retrieveDataFromTheMovieDb(MovieDbApi.TOP_RATED_MOVIES);
                break;
            case INTERNAL_MOVIE_STORE_FAVORITES:
                retrieveDataFromInternalMovieStore();
                break;
        }
    }

    private void retrieveDataFromTheMovieDb(MovieDbApi api) {
        JsonObjectRequest request = new JsonObjectRequest
                (GET, getMovieDbEndpoint(api).toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateViewAdapterWithRetrievedData(parsePopularMovies(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HttpRequestErrorResponseHandler.handleError(getContext(), error);
                    }
                });

        HttpRequestManager.getInstance(getContext()).addToRequestQueue(request);
    }

    private void retrieveDataFromInternalMovieStore() {
        final Cursor cursor =
                getContext()
                        .getContentResolver()
                        .query(FavoriteMoviesTable.CONTENT_URI, null, null, null, null);

        updateViewAdapterWithRetrievedData(
                Lists.transform(
                        FavoriteMoviesTable.getRows(cursor, true),
                        new Function<InternalMovie, Movie>() {
                            @Override
                            public Movie apply(InternalMovie internalMovie) {
                                return InternalMovieTypeConverter.toMovie(internalMovie);
                            }
                        }));
    }

    private void updateViewAdapterWithRetrievedData(List<Movie> retrievedMovies) {
        if (!retrievedMovies.isEmpty()) {
            movies.clear();
            movies.addAll(retrievedMovies);
            moviesRecyclerViewAdapter.notifyDataSetChanged();
            moviesRecyclerViewAdapter.notifyItemRangeChanged(0, retrievedMovies.size());

            if (isAdded() && getResources().getBoolean(R.bool.is_tablet)) {
                moviesRecyclerViewAdapter.gotoDetailPage(movies.get(0));
            }
        }
    }

    private void registerSortPreference(MovieDataSources api) {
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_sort_pref), api.name());
        editor.apply();
    }

    private MovieDataSources getPreferredMovieDataSource() {
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        String registeredApi = sharedPref.getString(getString(R.string.saved_sort_pref), THE_MOVIE_DB_POPULAR_MOVIES.name());

        return MovieDataSources.valueOf(registeredApi);
    }
}
