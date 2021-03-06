package com.example.coderchang.onedaily.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.db.CollectionDB;
import com.example.coderchang.onedaily.db.ImportantDatabaseHelper;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.NewsDetail;
import com.example.coderchang.onedaily.doman.SimpleStory;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.utils.HtmlUtil;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;

/**
 * Created by coderchang on 16/8/24.
 */
public class NewsDetailActivity extends BaseActivity{
    private WebView webViewNewsDetail;
    private Handler handler = new Handler();
    private Toolbar tbNewsDetail;
    private Story story;
    private TopStory topStory;
    private SimpleStory simpleStory;
    private CollectionDB collectionDB;
    private NewsDetail newsDetail;
    private boolean favorite =false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collectionDB = CollectionDB.getInstance(this);
        initView();
        initToolbar();
        Intent intent = getIntent();
        story = null;
        topStory = null;
        simpleStory = null;
        if (intent.getSerializableExtra("story") != null) {
            story = (Story) intent.getSerializableExtra("story");
            favorite = collectionDB.isFavorite(story);
        }
        if (intent.getSerializableExtra("topStory") != null) {
            topStory = (TopStory) intent.getSerializableExtra("topStory");
            favorite = collectionDB.isFavorite(topStory);
        }
        if (intent.getSerializableExtra("simpleStory") != null){
            simpleStory = (SimpleStory) intent.getSerializableExtra("simpleStory");
            favorite = collectionDB.isFavorite(simpleStory);
        }
        if (!NetUtil.netIsAvailable(this)) {
            Toast.makeText(this, "网络错误,请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (story != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + story.getId(), new NetUtil.StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gson gson = new Gson();
                            newsDetail = gson.fromJson(response, NewsDetail.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String htmlData = HtmlUtil.createHtmlData(newsDetail);
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
        if (topStory != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + topStory.getId(), new NetUtil.StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gson gson = new Gson();
                            newsDetail = gson.fromJson(response, NewsDetail.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String htmlData = HtmlUtil.createHtmlData(newsDetail);
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
        } if (simpleStory != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtil.syncStringGet("http://news-at.zhihu.com/api/4/news/" + simpleStory.getStoryId(), new NetUtil.StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gson gson = new Gson();
                            newsDetail = gson.fromJson(response, NewsDetail.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String htmlData = HtmlUtil.createHtmlData(newsDetail);
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
        if (favorite) {
            tbNewsDetail.getMenu().getItem(1).setIcon(R.drawable.fav_active);
        }



    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_news_detail;
    }

    private void initToolbar() {
        tbNewsDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tbNewsDetail.inflateMenu(R.menu.tool_bar_detail_menu);

        tbNewsDetail.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_detail_share:
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, newsDetail.getShare_url());
                        shareIntent.setType("text/plain");
                        //设置分享列表的标题，并且每次都显示分享列表
                        startActivity(Intent.createChooser(shareIntent, "分享到"));
                        break;
                    case R.id.action_detail_important:
                        if (!favorite) {
                            favorite = true;
                            item.setIcon(R.drawable.fav_active);
                            addStoryToDatabase();
                            Toast.makeText(NewsDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;

                }
                return true;
            }

        });
    }

    private void addStoryToDatabase() {
        if (topStory != null) {
            collectionDB.saveSimpleStory(topStory);
        }
        if (story != null) {
            collectionDB.saveSimpleStory(story);
        }
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
