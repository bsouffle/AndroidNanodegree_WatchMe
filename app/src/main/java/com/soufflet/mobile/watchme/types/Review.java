package com.soufflet.mobile.watchme.types;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Review implements Parcelable {

    public static Review create(String id, String author, String content) {
        return new AutoValue_Review(id, author, content);
    }

    public abstract String id();
    public abstract String author();
    public abstract String content();
}
