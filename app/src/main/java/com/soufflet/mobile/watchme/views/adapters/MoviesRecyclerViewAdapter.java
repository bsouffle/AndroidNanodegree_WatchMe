package com.soufflet.mobile.watchme.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.dao.FavoriteMoviesTable;
import com.soufflet.mobile.watchme.dao.InternalMovieTypeConverter;
import com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure;
import com.soufflet.mobile.watchme.detailpage.MovieOverviewActivity;
import com.soufflet.mobile.watchme.detailpage.MovieOverviewFragment;
import com.soufflet.mobile.watchme.types.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.MovieDbImageSize.WIDTH_342;
import static com.soufflet.mobile.watchme.adapters.moviedb.MovieDbApiUrlFactory.createMovieDbImageUriFor;
import static com.soufflet.mobile.watchme.types.Movie.MOVIE_EXTRA_PARCEL;

public class MoviesRecyclerViewAdapter
        extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder> {

    private FragmentManager supportFragmentManager;
    private final List<Movie> movies;
    private final Context context;
    private final Activity activity;

    public MoviesRecyclerViewAdapter(
            Activity activity,
            Context context,
            FragmentManager supportFragmentManager,
            List<Movie> movies) {
        this.activity = activity;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder viewHolder, final int position) {
        final Movie selectedMovie = movies.get(position);

        viewHolder.title.setText(selectedMovie.title());
        Picasso.with(context)
                .load(createMovieDbImageUriFor(selectedMovie.imagePath(), WIDTH_342))
                .into(viewHolder.poster);

        retrieveStateFromInternalDb(viewHolder, selectedMovie);

        setEventListeners(viewHolder, selectedMovie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void gotoDetailPage(Movie movie) {
        if (activity.getResources().getBoolean(R.bool.is_tablet)) {
            MovieOverviewFragment fragment = new MovieOverviewFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_EXTRA_PARCEL, movie);
            fragment.setArguments(bundle);

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.details_fragment_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(activity, MovieOverviewActivity.class);
            intent.putExtra(MOVIE_EXTRA_PARCEL, movie);

            activity.startActivity(intent);
        }
    }

    private void retrieveStateFromInternalDb(
            final MovieViewHolder viewHolder, final Movie selectedMovie) {
        final Cursor cursor = context.getContentResolver().query(
                FavoriteMoviesTable.CONTENT_URI,
                new String[]{InternalMoviesTableStructure.COLUMN_ID},
                InternalMoviesTableStructure.COLUMN_ID + " = " + selectedMovie.id(),
                null,
                null);

        if (cursor.moveToFirst()) {
            viewHolder.savedAsFavorite();
        } else {
            viewHolder.unsavedAsFavorite();
        }
    }

    private void setEventListeners(
            final MovieViewHolder viewHolder, final Movie selectedMovie) {
        viewHolder.notStoredAsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getContentResolver().insert(
                        FavoriteMoviesTable.CONTENT_URI,
                        FavoriteMoviesTable.getContentValues(
                                InternalMovieTypeConverter.fromMovie(selectedMovie), true));

                viewHolder.savedAsFavorite();
            }
        });

        viewHolder.storedAsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getContentResolver().delete(
                        FavoriteMoviesTable.CONTENT_URI,
                        InternalMoviesTableStructure.COLUMN_ID + " = ?",
                        new String[]{ String.valueOf(selectedMovie.id()) });

                viewHolder.unsavedAsFavorite();
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetailPage(selectedMovie);
            }
        });
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_card_view) CardView cardView;
        @BindView(R.id.movie_title) TextView title;
        @BindView(R.id.movie_poster) ImageView poster;
        @BindView(R.id.movie_stored_as_fav_icon) ImageView storedAsFav;
        @BindView(R.id.movie_not_stored_as_fav_icon) ImageView notStoredAsFav;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void savedAsFavorite() {
            storedAsFav.setVisibility(VISIBLE);
            notStoredAsFav.setVisibility(GONE);
        }

        public void unsavedAsFavorite() {
            storedAsFav.setVisibility(GONE);
            notStoredAsFav.setVisibility(VISIBLE);
        }
    }
}
