package com.example.coderchang.onedaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.NewsDetail;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;

/**
 * Created by coderchang on 16/8/24.
 */
public class NewsDetailActivity extends AppCompatActivity{
    private WebView webViewNewsDetail;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        initView();
        Intent intent = getIntent();
        final Story story = (Story) intent.getSerializableExtra("story");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + story.getId(), new NetUtil.StringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Gson gson = new Gson();
                        final NewsDetail detail = gson.fromJson(response, NewsDetail.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                webViewNewsDetail.loadDataWithBaseURL(null, detail.getBody(), "text/html", "utf-8", null);
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            }
        }).start();
    }

    private void initView() {
        webViewNewsDetail = (WebView) findViewById(R.id.web_view_news_detail);
    }
}
