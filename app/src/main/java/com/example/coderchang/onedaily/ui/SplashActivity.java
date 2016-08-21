package com.example.coderchang.onedaily.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coderchang.onedaily.MainActivity;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.utils.NetUtil;

/**
 * Created by coderchang on 16/8/21.
 */
public class SplashActivity extends AppCompatActivity{

    private ImageView ivSplashPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
        NetUtil.asyncGet("https://pic1.zhimg.com//v2-9639852750175df1b80ed995729e64e8.jpg", new NetUtil.Callback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivSplashPic.setImageBitmap(bitmap);
                try {
                    Thread.sleep(3000);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(SplashActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        ivSplashPic = (ImageView) findViewById(R.id.iv_splash_pic);

    }
}
