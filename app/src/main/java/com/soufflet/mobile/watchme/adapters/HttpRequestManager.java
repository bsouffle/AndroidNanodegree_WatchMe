package com.soufflet.mobile.watchme.adapters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class HttpRequestManager {

    private static HttpRequestManager INSTANCE;
    private RequestQueue requestQueue;
    private Context context;

    private HttpRequestManager(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequestManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestManager(context);
        }

        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
