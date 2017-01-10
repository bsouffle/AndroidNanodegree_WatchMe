package com.soufflet.mobile.watchme.adapters;

import android.net.Uri;

import com.soufflet.mobile.watchme.types.YoutubeTrailer;

public final class YoutubeUriFactory {

    private YoutubeUriFactory() {}

    public static Uri createYoutubeImgUri(YoutubeTrailer trailer) {
        return Uri.parse("http://img.youtube.com/vi/" + trailer.key() + "/mqdefault.jpg");
    }

    public static Uri createYoutubeAppUri(YoutubeTrailer trailer) {
        return Uri.parse("vnd.youtube:" + trailer.key());
    }

    public static Uri createYoutubeWebsiteUri(YoutubeTrailer trailer) {
        return Uri.parse("http://www.youtube.com/watch?v=" + trailer.key());
    }
}
