package com.example.coderchang.onedaily;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.coderchang.onedaily.ui.fragment.GankFragment;
import com.example.coderchang.onedaily.ui.fragment.MainFragment;
import com.example.coderchang.onedaily.utils.DateUtils;
import com.example.coderchang.onedaily.utils.GsonRequest;
import com.example.coderchang.onedaily.utils.NetUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity{
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FragmentManager fm;
    private MainFragment mainFragment;
    private GankFragment gankFragment;
    private Fragment tmpFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initToolbar();
        initNavigationView();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!NetUtil.netIsAvailable(this)){
            Toast.makeText(this, "网络错误,请检查网络",Toast.LENGTH_SHORT).show();
            return;
        }
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        transaction.add(R.id.main_frame_layout, mainFragment).commit();
        tmpFragment = mainFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


//    private void formatUrl(List<Story> storyList) {
//        for (int i = 0; i < storyList.size(); i++) {
//            String formattedUrl = storyList.get(i).getImages().get(0).replaceAll("\"", "");
//            storyList.get(i).getImages().set(0, formattedUrl);
//        }
//    }



    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                switch (item.getItemId()) {
                    case R.id.navigation_item_main:
                        toolbar.setTitle("首页");
                        if (mainFragment == null) {
                            mainFragment = new MainFragment();
                        }
                        switchFragment(mainFragment);
                        break;
                    case R.id.navigation_item_guoke:
                        toolbar.setTitle("果壳");
                        Toast.makeText(MainActivity.this, "guoke", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_douban:
                        toolbar.setTitle("豆瓣");
                        Toast.makeText(MainActivity.this, "douban", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_important:
                        Toast.makeText(MainActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MyCollectionActivity.class));
                        break;
                    case R.id.navigation_item_gank:
                        toolbar.setTitle("福利");
                        if (gankFragment == null) {
                            gankFragment = new GankFragment();
                        }
                        switchFragment(gankFragment);
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

    private void switchFragment(Fragment currentFragment) {
        if (tmpFragment != currentFragment) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (!currentFragment.isAdded()) {
                transaction.hide(tmpFragment).add(R.id.main_frame_layout, currentFragment).commit();
            } else {
                transaction.hide(tmpFragment).show(currentFragment).commit();
            }
            tmpFragment = currentFragment;
        }
    }
}
