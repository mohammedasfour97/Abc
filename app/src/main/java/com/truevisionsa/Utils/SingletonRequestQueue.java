package com.truevisionsa.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequestQueue {
    private static SingletonRequestQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String url;

    private SingletonRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        url = getUrl();
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
    public String getUrl(){
        return "http://" + new DatabaseHelper(mContext).getUser().get(0).getWebIp() + ":" + new DatabaseHelper(mContext).getUser().get(0).getWebPort()
              // "http://abcapi.dyndns.org:40809"
                       +
                "/api/";
    }
}

