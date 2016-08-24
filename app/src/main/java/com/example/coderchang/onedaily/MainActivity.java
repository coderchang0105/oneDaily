package com.example.coderchang.onedaily;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.coderchang.onedaily.adapter.CarouselAdapter;
import com.example.coderchang.onedaily.adapter.RVMainAdapter;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.utils.MyThread;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView rvMainDaily;
    private RVMainAdapter adapter;
    private News news;

    private CarouselAdapter carouselAdapter;

    private View carouselView;

    private List<Story> storyList = new ArrayList<>();

    private List<TopStory> topStoryList = new ArrayList<>();

    private ViewPager vpMainCarousel;

    private boolean loading = false;//是否在上拉加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        carouselView = LayoutInflater.from(this).inflate(R.layout.layout_main_carousel, null);
        initToolbar();
        initNavigationView();
        initView();
        initData();
        adapter = new RVMainAdapter(MainActivity.this, storyList);
        adapter.setCarouselView(carouselView);
        rvMainDaily.setLayoutManager(new LinearLayoutManager(this));
        rvMainDaily.setAdapter(adapter);
        carouselAdapter = new CarouselAdapter(MainActivity.this, topStoryList);
        vpMainCarousel.setOffscreenPageLimit(3);
        vpMainCarousel.setCurrentItem(0);
        vpMainCarousel.setAdapter(carouselAdapter);

        adapter.setOnLoadListener(new RVMainAdapter.OnLoadListener() {
            @Override
            public void onLoad() {
                new StoryTask().execute("http://news.at.zhihu.com/api/4/news/before/20160823");
            }
        });

    }

    private void initData() {
        MyThread myThread = new MyThread("http://news-at.zhihu.com/api/4/news/latest", new NetUtil.StringCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("TAG", "news latest = " + response);
                //parseJSONWithJSONObject(response);

                parseJSONWithGson(response);
                formatUrl(storyList);
                for (int i = 0; i < storyList.size(); i++) {
                    Log.d("TAG", "formatted story = " + storyList.get(i).getImages().get(0));

                }


            }

            @Override
            public void onFail(Exception e) {

            }
        });
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONWithGson(String response) {
        Gson gson = new Gson();
        News news = gson.fromJson(response, News.class);
        storyList = news.getStories();
        topStoryList = news.getTop_stories();

    }



    private void formatUrl(List<Story> storyList) {
        for (int i = 0; i < storyList.size(); i++) {
            String formattedUrl = storyList.get(i).getImages().get(0).replaceAll("\"", "");
            storyList.get(i).getImages().set(0, formattedUrl);
        }
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rvMainDaily = (RecyclerView) findViewById(R.id.rv_main_daily);
        vpMainCarousel = (ViewPager) carouselView.findViewById(R.id.vp_main_carousel);
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                switch (item.getItemId()) {
                    case R.id.navigation_item_guoke:
                        Toast.makeText(MainActivity.this, "guoke", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_douban:
                        Toast.makeText(MainActivity.this, "douban", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.inflateMenu(R.menu.tool_bar_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_menu_night_mode:
                        Toast.makeText(MainActivity.this, "night mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_menu_setting:
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });
    }


    class StoryTask extends AsyncTask<String, Void, List<Story>> {
        private List<Story> tempStoryList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Story> doInBackground(String... params) {

            NetUtil.syncStringGet(params[0], new NetUtil.StringCallback() {
                @Override
                public void onSuccess(String response) {
                    Gson gson = new Gson();
                    News news = gson.fromJson(response, News.class);
                    tempStoryList = news.getStories();
                    formatUrl(tempStoryList);
                }

                @Override
                public void onFail(Exception e) {

                }
            });
            return tempStoryList;
        }

        @Override
        protected void onPostExecute(List<Story> stories) {
            super.onPostExecute(stories);
            adapter.addData(stories);
        }
    }




    private void parseJSONWithJSONObject(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);
                Story story = new Story();
                story.setTitle(object1.getString("title"));
                story.setGa_prefix(object1.getString("ga_prefix"));
                JSONArray arrayImages = object1.getJSONArray("images");
                List<String> images = new ArrayList<>();
                for (int j = 0; j < arrayImages.length(); j++) {
                    images.add(arrayImages.getString(j));
                }
                story.setImages(images);
                story.setType(object1.getInt("type"));
                story.setId(object1.getInt("id"));
                storyList.add(story);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
