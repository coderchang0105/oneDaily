package com.example.coderchang.onedaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.NewsDetail;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.utils.HtmlUtil;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;

/**
 * Created by coderchang on 16/8/24.
 */
public class NewsDetailActivity extends AppCompatActivity{
    private WebView webViewNewsDetail;
    private Handler handler = new Handler();
    private Toolbar tbNewsDetail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        initView();
        Intent intent = getIntent();
        Story story = null;
        TopStory topStory = null;
        if (intent.getSerializableExtra("story") == null) {
            topStory = (TopStory) intent.getSerializableExtra("topStory");
        }else {
            story = (Story) intent.getSerializableExtra("story");
        }
        if (story == null) {
            final TopStory finalTopStory = topStory;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + finalTopStory.getId(), new NetUtil.StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gson gson = new Gson();
                            final NewsDetail detail = gson.fromJson(response, NewsDetail.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String htmlData = HtmlUtil.createHtmlData(detail);
                                    webViewNewsDetail.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                                }
                            });
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                }
            }).start();
        }else {
            final Story finalStory = story;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + finalStory.getId(), new NetUtil.StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gson gson = new Gson();
                            final NewsDetail detail = gson.fromJson(response, NewsDetail.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String htmlData = HtmlUtil.createHtmlData(detail);
                                    webViewNewsDetail.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
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

        tbNewsDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        webViewNewsDetail = (WebView) findViewById(R.id.web_view_news_detail);
        webViewNewsDetail.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webViewNewsDetail.getSettings().setLoadsImagesAutomatically(true);
        //设置 缓存模式
        webViewNewsDetail.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webViewNewsDetail.getSettings().setDomStorageEnabled(true);
        tbNewsDetail = (Toolbar) findViewById(R.id.tb_news_detail);
    }
}
