package com.example.coderchang.onedaily.utils;

/**
 * Created by coderchang on 16/8/22.
 */
public class MyThread extends Thread {
    private String url;
    private NetUtil.StringCallback callback;
    public MyThread(String url,NetUtil.StringCallback callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override
    public void run() {
        NetUtil.syncStringGet(url,callback);
    }
}
