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

import com.example.coderchang.onedaily.MainActivity;
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

    public static final int SEND = 1;
    private ImageView ivSplashPic;

    private SplashPic splashPic;

    private TextView tvSplashName;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND:
                    ivSplashPic.setImageBitmap((Bitmap) msg.obj);
                    tvSplashName.setText(splashPic.getText());
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        MyThread thread = new MyThread(SPLASH_URL, new NetUtil.StringCallback() {

            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                splashPic = gson.fromJson(response, SplashPic.class);
                String url = splashPic.getImg().replaceAll("\"", "");
                splashPic.setImg(url);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(SplashActivity.this, "图片地址加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NetUtil.asyncImageGet(splashPic.getImg(), new NetUtil.ImageCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Message message = new Message();
                message.what = SEND;
                message.obj = bitmap;
                handler.sendMessage(message);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(SplashActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
            }
        });

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
