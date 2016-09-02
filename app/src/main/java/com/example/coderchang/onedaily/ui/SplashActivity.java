package com.example.coderchang.onedaily.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coderchang.onedaily.MainActivity;
import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.SplashPic;
import com.example.coderchang.onedaily.utils.MyThread;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;

/**
 * Created by coderchang on 16/8/21.
 */
public class SplashActivity extends BaseActivity {
    public static final String SPLASH_URL = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    private ImageView ivSplashPic;

    private SplashPic splashPic;

    private TextView tvSplashName;

    private MyApplication helper;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = MyApplication.getInstance();
        initView();
        StringRequest strRequest = new StringRequest(SPLASH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                splashPic = gson.fromJson(s, SplashPic.class);
                ImageRequest imgRequest = new ImageRequest(splashPic.getImg(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivSplashPic.setImageBitmap(bitmap);
                        tvSplashName.setText(splashPic.getText());
                        try {
                            Thread.currentThread().sleep(3000);
                            finish();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                helper.add(imgRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        helper.add(strRequest);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    private void initView() {
        ivSplashPic = (ImageView) findViewById(R.id.iv_splash_pic);
        tvSplashName = (TextView) findViewById(R.id.tv_splash_name);

    }

}
