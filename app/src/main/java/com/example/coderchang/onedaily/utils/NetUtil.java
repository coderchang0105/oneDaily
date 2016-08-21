package com.example.coderchang.onedaily.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.telecom.Call;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by coderchang on 16/8/21.
 */
public class NetUtil {
    public interface Callback{
        void onSuccess(Bitmap bitmap);

        void onFail(Exception e);
    }

    public static InputStream getInputStream(String url) throws Exception{
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

    private static String getStringFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer);
        }
        is.close();
        String data = os.toString();
        os.close();
        return data;
    }


    public static void asyncGet(final String url, final Callback callback){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getInputStream(url);
                    final Bitmap bitmap = getBitmapFromInputStream(is);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onSuccess(bitmap);
                            }
                        }
                    });
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFail(e);
                    }
                }
            }
        }).start();
    }
}
