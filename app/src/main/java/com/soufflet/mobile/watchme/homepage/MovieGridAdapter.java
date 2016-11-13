package com.soufflet.mobile.watchme.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.soufflet.mobile.watchme.types.Movie;
import com.squareup.picasso.Picasso;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private LayoutInflater inflater;
    private int imageViewResourceId;

    public MovieGridAdapter(Context context, int resourceId, int imageViewResourceId) {
        super(context, resourceId);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imageViewResourceId = imageViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(imageViewResourceId, parent, false);
        }

        Picasso.with(context)
                .load(getItem(position).getImageUri())
                .fit()
                .into((ImageView) convertView);

        return convertView;
    }
}
