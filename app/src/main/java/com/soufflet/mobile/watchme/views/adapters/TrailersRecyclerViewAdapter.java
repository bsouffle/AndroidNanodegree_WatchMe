package com.soufflet.mobile.watchme.views.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soufflet.mobile.watchme.R;
import com.soufflet.mobile.watchme.types.YoutubeTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.ACTION_VIEW;
import static com.soufflet.mobile.watchme.adapters.YoutubeUriFactory.createYoutubeAppUri;
import static com.soufflet.mobile.watchme.adapters.YoutubeUriFactory.createYoutubeImgUri;
import static com.soufflet.mobile.watchme.adapters.YoutubeUriFactory.createYoutubeWebsiteUri;

public class TrailersRecyclerViewAdapter
        extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.TrailerViewHolder> {

    private final Context context;
    private final List<YoutubeTrailer> trailers;

    public TrailersRecyclerViewAdapter(Context context, List<YoutubeTrailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailersRecyclerViewAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailersRecyclerViewAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder viewHolder, int position) {
        final YoutubeTrailer selectedTrailer = trailers.get(position);

        viewHolder.trailerTitle.setText(selectedTrailer.name());
        Picasso.with(context)
                .load(createYoutubeImgUri(selectedTrailer))
                .into(viewHolder.trailerImg);

        viewHolder.trailerItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideoTrailer(selectedTrailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    private void playVideoTrailer(YoutubeTrailer trailer) {
        try {
            context.startActivity(
                    new Intent(ACTION_VIEW, createYoutubeAppUri(trailer)));
        } catch (ActivityNotFoundException ex) {
            context.startActivity(
                    new Intent(ACTION_VIEW, createYoutubeWebsiteUri(trailer)));
        }
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_title) TextView trailerTitle;
        @BindView(R.id.trailer_img) ImageView trailerImg;
        @BindView(R.id.trailer_item_layout) LinearLayout trailerItemLayout;

        public TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }
}
