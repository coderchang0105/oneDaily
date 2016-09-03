package com.example.coderchang.onedaily.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.adapter.CarouselAdapter;
import com.example.coderchang.onedaily.adapter.RVMainAdapter;
import com.example.coderchang.onedaily.doman.News;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.ui.NewsDetailActivity;
import com.example.coderchang.onedaily.utils.DateUtils;
import com.example.coderchang.onedaily.utils.GsonRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by coderchang on 16/9/2.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvMainDaily;
    private SwipeRefreshLayout refreshLayout;
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
    private View view;
    private boolean isFirstEnter = true;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        helper = MyApplication.getInstance();
        carouselView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_carousel, null);
        initView();
        initPointsGroup();
        setRefreshLayout();
        addTimerTask();
        adapter = new RVMainAdapter(getActivity());
        adapter.setCarouselView(carouselView);
        rvMainDaily.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMainDaily.setAdapter(adapter);
        setCarouselAdapter();
        setCarousel();
        sendANewsTask(LATEST_URL);
        return view;
    }

    private void initView() {

        rvMainDaily = (RecyclerView) view.findViewById(R.id.rv_main_daily);
        vpMainCarousel = (ViewPager) carouselView.findViewById(R.id.vp_main_carousel);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    private void setRefreshLayout() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setRefreshing(true);
    }

    private void setCarouselAdapter() {
        carouselAdapter = new CarouselAdapter(getActivity());
        carouselAdapter.setOnItemClickListener(new CarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TopStory topStory) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("topStory", topStory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new RVMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Story story) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
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
                    date = DateUtils.preDate(date);
                    sendANewsTask(BEFORE_URL + date);
                }
            }
        });
    }

    private void sendANewsTask(String url) {
        GsonRequest<News> gsonRequest = new GsonRequest<>(Request.Method.GET, url, new Response.ErrorListener() {
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
                    if (isFirstEnter) {
                        isFirstEnter = false;
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });
        helper.add(gsonRequest);
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
        }, 0, 7000);
    }

    private void initPointsGroup() {
        llPoints = (LinearLayout) carouselView.findViewById(R.id.ll_select_points);
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(getActivity());
            if (i == 0) {
                imageView.setImageResource(R.mipmap.page_indicator_focused);
            } else {
                imageView.setImageResource(R.mipmap.page_indicator_unfocused);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            llPoints.addView(imageView, layoutParams);
        }
    }

    private Timer timer;

    private void resetIndicator() {
        for (int i = 0; i < llPoints.getChildCount(); i++) {
            ImageView imageView = (ImageView) llPoints.getChildAt(i);
            imageView.setImageResource(R.mipmap.page_indicator_unfocused);
        }
    }

    @Override
    public void onRefresh() {
        if (!isFirstEnter) {
            refreshLayout.setRefreshing(false);
        }
    }
}
