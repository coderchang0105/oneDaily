package com.example.coderchang.onedaily.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telecom.Call;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by coderchang on 16/8/21.
 */
public class NetUtil {

    public NetUtil() {
    }

    public interface ImageCallback {
        void onSuccess(Bitmap bitmap);

        void onFail(Exception e);
    }

    public interface StringCallback {
        void onSuccess(String response);

        void onFail(Exception e);
    }

    public static InputStream getInputStream(String url) throws Exception {
        HttpURLConnection connection = null;
        URL mUrl = new URL(url);
        connection = (HttpURLConnection) mUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            InputStream is = connection.getInputStream();
            return is;
        }
        connection.disconnect();
        return null;
    }


    public static Bitmap getBitmapFromInputStream(InputStream inputStream) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        inputStreamReader = new InputStreamReader(is, "utf-8");
        reader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = reader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }


    public static void asyncImageGet(final String url, final ImageCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getInputStream(url);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (callback != null) {
                        callback.onSuccess(bitmap);
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFail(e);
                    }
                }
            }
        }).start();
    }

    public static void syncStringGet(final String url, final StringCallback callback) {
        InputStream is = null;
        try {
            is = getInputStream(url);
            String response = getStringFromInputStream(is);
            if (callback != null) {
                callback.onSuccess(response);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onFail(e);
            }
        }
    }

    public static boolean netIsAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
          if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}

