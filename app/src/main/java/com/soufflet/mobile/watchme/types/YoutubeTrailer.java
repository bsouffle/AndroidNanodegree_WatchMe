package com.soufflet.mobile.watchme.types;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class YoutubeTrailer implements Parcelable {

    public static YoutubeTrailer create(String id, String name, String key) {
        return new AutoValue_YoutubeTrailer(id, name, key);
    }

    public abstract String id();
    public abstract String name();
    public abstract String key();
}
