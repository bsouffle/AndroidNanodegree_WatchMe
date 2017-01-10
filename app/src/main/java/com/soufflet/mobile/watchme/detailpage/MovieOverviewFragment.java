package com.soufflet.mobile.watchme.detailpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.collect.ImmutableList;
import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.adapters.HttpRequestManager;
import com.soufflet.mobile.watchme.types.Movie;
import com.soufflet.mobile.watchme.types.Review;
import com.soufflet.mobile.watchme.types.YoutubeTrailer;
import com.soufflet.mobile.watchme.utils.HttpRequestErrorResponseHandler;
import com.soufflet.mobile.watchme.views.adapters.ReviewsRecyclerViewAdapter;
import com.soufflet.mobile.watchme.views.adapters.TrailersRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.android.volley.Request.Method.GET;
import static com.google.common.collect.Lists.newArrayList;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbImageSize.WIDTH_342;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createMovieDbImageUriFor;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createReviewsEndpoint;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createTrailersEndpoint;
import static com.soufflet.mobile.watchme.adapters.moviedb.parsers.MovieDbReviewResponseParser.parseReviews;
import static com.soufflet.mobile.watchme.adapters.moviedb.parsers.MovieDbTrailerResponseParser.parseTrailers;
import static com.soufflet.mobile.watchme.types.Movie.MOVIE_EXTRA_PARCEL;

public class MovieOverviewFragment extends Fragment {

    @BindView(R.id.movie_image) ImageView posterView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_description) TextView descriptionView;
    @BindView(R.id.movie_rating) TextView ratingView;
    @BindView(R.id.movie_date) TextView dateView;
    @BindView(R.id.movie_trailers_layout) LinearLayout trailersLayout;
    @BindView(R.id.movie_reviews_layout) LinearLayout reviewsLayout;
    @BindView(R.id.trailers_recycler_view) RecyclerView trailersRecyclerView;
    @BindView(R.id.reviews_recycler_view) RecyclerView reviewsRecyclerView;

    private final DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
    private final List<Review> reviews = newArrayList();
    private final List<YoutubeTrailer> trailers = newArrayList();

    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private TrailersRecyclerViewAdapter trailersRecyclerViewAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, rootView);

        reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(getContext(), reviews);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsRecyclerView.setAdapter(reviewsRecyclerViewAdapter);

        trailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(getContext(), trailers);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trailersRecyclerView.setAdapter(trailersRecyclerViewAdapter);

        Movie selectedMovie = retrieveMovieParcel();
        if (selectedMovie != null) {
            Picasso.with(getContext())
                    .load(createMovieDbImageUriFor(selectedMovie.imagePath(), WIDTH_342))
                    .into(posterView);

            titleView.setText(selectedMovie.title());
            descriptionView.setText(selectedMovie.overview());
            ratingView.setText(Double.toString(selectedMovie.rating()));
            dateView.setText(dateFormat.format(selectedMovie.releaseDate()));

            fetchAndDisplayReviewData(selectedMovie.id());
            fetchAndDisplayTrailerData(selectedMovie.id());
        }

        return rootView;
    }

    private Movie retrieveMovieParcel() {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MOVIE_EXTRA_PARCEL)) {
            return intent.getParcelableExtra(MOVIE_EXTRA_PARCEL);
        }

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MOVIE_EXTRA_PARCEL)) {
            return bundle.getParcelable(MOVIE_EXTRA_PARCEL);
        }

        return null;
    }

    private void fetchAndDisplayReviewData(long movieId) {
        JsonObjectRequest request = new JsonObjectRequest
                (GET, createReviewsEndpoint(movieId).toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ImmutableList<Review> parsedReviews = parseReviews(response);
                        if (parsedReviews.isEmpty()) {
                            reviewsLayout.setVisibility(GONE);
                        } else {
                            reviewsLayout.setVisibility(VISIBLE);
                            reviews.clear();
                            reviews.addAll(parsedReviews);
                            reviewsRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HttpRequestErrorResponseHandler.handleError(getContext(), error);
                    }
                });

        HttpRequestManager.getInstance(getContext()).addToRequestQueue(request);
    }

    private void fetchAndDisplayTrailerData(long movieId) {
        JsonObjectRequest request = new JsonObjectRequest
                (GET, createTrailersEndpoint(movieId).toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ImmutableList<YoutubeTrailer> parsedTrailers = parseTrailers(response);
                        if (parsedTrailers.isEmpty()) {
                            trailersLayout.setVisibility(GONE);
                        } else {
                            trailersLayout.setVisibility(VISIBLE);
                            trailers.clear();
                            trailers.addAll(parsedTrailers);
                            trailersRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HttpRequestErrorResponseHandler.handleError(getContext(), error);
                    }
                });

        HttpRequestManager.getInstance(getContext()).addToRequestQueue(request);
    }
}
