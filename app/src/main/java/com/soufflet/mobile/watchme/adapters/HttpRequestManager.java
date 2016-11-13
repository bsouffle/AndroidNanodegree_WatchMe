package com.soufflet.mobile.watchme.adapters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class HttpRequestManager {

    private static HttpRequestManager INSTANCE;

    private final RequestQueue requestQueue;

    private HttpRequestManager(Context context) {
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized HttpRequestManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestManager(context);
        }

        return INSTANCE;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }
}
