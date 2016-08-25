package com.example.coderchang.onedaily;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.coderchang.onedaily.adapter.CarouselAdapter;
import com.example.coderchang.onedaily.adapter.RVMainAdapter;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.ui.NewsDetailActivity;
import com.example.coderchang.onedaily.utils.DateUtils;
import com.example.coderchang.onedaily.utils.MyThread;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView rvMainDaily;
    private RVMainAdapter adapter;
    private News news;
    private int currentItem;

    private LinearLayout llPoints;

    private Handler handler = new Handler();

    private CarouselAdapter carouselAdapter;

    private View carouselView;

    private List<Story> storyList = new ArrayList<>();

    private List<TopStory> topStoryList = new ArrayList<>();

    private ViewPager vpMainCarousel;

    private boolean loading = false;//是否在上拉加载

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        carouselView = LayoutInflater.from(this).inflate(R.layout.layout_main_carousel, null);
        initToolbar();
        initNavigationView();
        initView();
        initPointsGroup();
        addTimerTask();
        initData();
        adapter = new RVMainAdapter(MainActivity.this, storyList);
        adapter.setCarouselView(carouselView);
        rvMainDaily.setLayoutManager(new LinearLayoutManager(this));
        rvMainDaily.setAdapter(adapter);
        carouselAdapter = new CarouselAdapter(MainActivity.this, topStoryList);
        carouselAdapter.setOnItemClickListener(new CarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TopStory topStory) {
                Toast.makeText(MainActivity.this, topStory.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("topStory", topStory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        vpMainCarousel.setOffscreenPageLimit(3);
        vpMainCarousel.setCurrentItem(0);
        vpMainCarousel.setAdapter(carouselAdapter);
        vpMainCarousel.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                resetIndicator();
                ImageView imageView = (ImageView) llPoints.getChildAt(position);
                imageView.setImageResource(R.mipmap.page_indicator_focused);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter.setOnLoadListener(new RVMainAdapter.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.d("TAG", "date = " + date);
                date = DateUtils.preDate(date);
                new StoryTask().execute("http://news.at.zhihu.com/api/4/news/before/" + date);
            }
        });
        adapter.setOnItemClickListener(new RVMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Story story) {
                Toast.makeText(MainActivity.this, story.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("story", story);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void addTimerTask() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetIndicator();
                        if (currentItem == 4) {
                            currentItem = 0;
                            vpMainCarousel.setCurrentItem(0);
                        } else {
                            currentItem++;
                            vpMainCarousel.setCurrentItem(currentItem);

                        }
                        ImageView imageView = (ImageView) llPoints.getChildAt(currentItem);
                        imageView.setImageResource(R.mipmap.page_indicator_focused);
                    }
                });
            }
        },0,7000);
    }

    private void initPointsGroup() {
        llPoints = (LinearLayout) carouselView.findViewById(R.id.ll_select_points);
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.mipmap.page_indicator_focused);
            } else {
                imageView.setImageResource(R.mipmap.page_indicator_unfocused);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            llPoints.addView(imageView,layoutParams);
        }
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
        date = news.getDate();
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

    private Timer timer;

    private void resetIndicator() {
        for (int i = 0; i < llPoints.getChildCount(); i++) {
            ImageView imageView = (ImageView) llPoints.getChildAt(i);
            imageView.setImageResource(R.mipmap.page_indicator_unfocused);
        }
    }
}
