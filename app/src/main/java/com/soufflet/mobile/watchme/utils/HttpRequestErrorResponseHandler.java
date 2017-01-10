package com.soufflet.mobile.watchme.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NoConnectionError;

import static android.widget.Toast.LENGTH_SHORT;

public final class HttpRequestErrorResponseHandler {

    private HttpRequestErrorResponseHandler() {}

    public static void handleError(Context context, Exception exception) {
        final String errorMessage;
        if (exception instanceof NoConnectionError) {
            errorMessage = "No internet found. Please check your connection and try again.";
        } else {
            errorMessage = "Unexpected error. We were unable to fetch movies data.";
        }

        Toast.makeText(context, errorMessage, LENGTH_SHORT).show();
    }
}
