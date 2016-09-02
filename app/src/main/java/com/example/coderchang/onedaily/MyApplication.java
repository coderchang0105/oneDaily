package com.example.coderchang.onedaily;

import android.app.Application;
import android.content.Context;
import android.nfc.Tag;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by coderchang on 16/8/26.
 */
public class MyApplication extends Application {
    private RequestQueue mQueue;
    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mQueue;
    }


    public <T> void add(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancel() {
        mQueue.cancelAll(TAG);
    }

}
