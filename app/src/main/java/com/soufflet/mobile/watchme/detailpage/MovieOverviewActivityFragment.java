package com.soufflet.mobile.watchme.detailpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.types.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbImageSize.WIDTH_342;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createMovieDbImageUriFor;
import static com.soufflet.mobile.watchme.types.Movie.MOVIE_EXTRA_PARCEL;

/**
 * - Display detailed information about a selected movie.
 */
public class MovieOverviewActivityFragment extends Fragment {

    private final DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

    @BindView(R.id.movie_image) ImageView posterView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_description) TextView descriptionView;
    @BindView(R.id.movie_rating) TextView ratingView;
    @BindView(R.id.movie_date) TextView dateView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MOVIE_EXTRA_PARCEL)) {
            Movie selectedMovie = Parcels.unwrap(intent.getParcelableExtra(MOVIE_EXTRA_PARCEL));

            Picasso.with(getContext())
                    .load(createMovieDbImageUriFor(selectedMovie.getImagePath(), WIDTH_342))
                    .into(posterView);

            titleView.setText(selectedMovie.getTitle());
            descriptionView.setText(selectedMovie.getOverview());
            ratingView.setText(Double.toString(selectedMovie.getRating()));
            dateView.setText(dateFormat.format(selectedMovie.getReleaseDate()));
        }

        return rootView;
    }
}
