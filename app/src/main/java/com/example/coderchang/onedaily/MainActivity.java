package com.example.coderchang.onedaily;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coderchang.onedaily.adapter.CarouselAdapter;
import com.example.coderchang.onedaily.adapter.RVMainAdapter;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.ui.BaseActivity;
import com.example.coderchang.onedaily.ui.MyCollectionActivity;
import com.example.coderchang.onedaily.ui.NewsDetailActivity;
import com.example.coderchang.onedaily.utils.DateUtils;
import com.example.coderchang.onedaily.utils.GsonRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView rvMainDaily;
    private RVMainAdapter adapter;
    private MyApplication helper;
    public static final String LATEST_URL = "http://news-at.zhihu.com/api/4/news/latest";
    public static final String BEFORE_URL = "http://news.at.zhihu.com/api/4/news/before/";
    private int currentItem;

    private LinearLayout llPoints;

    private Handler handler = new Handler();

    private CarouselAdapter carouselAdapter;

    private View carouselView;

    private ViewPager vpMainCarousel;

    private boolean loading = false;//是否在上拉加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = MyApplication.getInstance();
        carouselView = LayoutInflater.from(this).inflate(R.layout.layout_main_carousel, null);
        initToolbar();
        initNavigationView();
        initView();
        initPointsGroup();
        addTimerTask();
        adapter = new RVMainAdapter(MainActivity.this);
        adapter.setCarouselView(carouselView);
        rvMainDaily.setLayoutManager(new LinearLayoutManager(this));
        rvMainDaily.setAdapter(adapter);
        setCarouselAdapter();
        setCarousel();
        sendANewsTask(LATEST_URL);
    }

    private void setCarouselAdapter() {
        carouselAdapter = new CarouselAdapter(MainActivity.this);
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

    private void setCarousel() {
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
            public void onLoad(String date) {
                if (date != null) {
                    Log.d("TAG", "date = " + date);
                    date = DateUtils.preDate(date);
                    sendANewsTask(BEFORE_URL + date);
                }
            }
        });
    }

    private void sendANewsTask(String url) {
        GsonRequest<News> gsonRequest = new GsonRequest<>(Request.Method.GET,url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, News.class, new Response.Listener<News>() {
            @Override
            public void onResponse(News news) {
                List<Story> storyList = news.getStories();
                List<TopStory> topStoryList = news.getTop_stories();
                adapter.addData(storyList);
                adapter.setDate(news.getDate());
                if (carouselAdapter.getData().size() == 0) {
                    carouselAdapter.addData(topStoryList);
                }
            }
        });
        helper.add(gsonRequest);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
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




//    private void formatUrl(List<Story> storyList) {
//        for (int i = 0; i < storyList.size(); i++) {
//            String formattedUrl = storyList.get(i).getImages().get(0).replaceAll("\"", "");
//            storyList.get(i).getImages().set(0, formattedUrl);
//        }
//    }

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
                    case R.id.navigation_item_important:
                        Toast.makeText(MainActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MyCollectionActivity.class));
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


    private Timer timer;

    private void resetIndicator() {
        for (int i = 0; i < llPoints.getChildCount(); i++) {
            ImageView imageView = (ImageView) llPoints.getChildAt(i);
            imageView.setImageResource(R.mipmap.page_indicator_unfocused);
        }
    }
}
