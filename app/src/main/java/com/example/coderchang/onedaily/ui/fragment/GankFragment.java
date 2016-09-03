package com.example.coderchang.onedaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.adapter.GankAdapter;
import com.example.coderchang.onedaily.doman.GankFuli;
import com.example.coderchang.onedaily.doman.GankResult;
import com.example.coderchang.onedaily.utils.GsonRequest;

import java.util.List;

/**
 * Created by coderchang on 16/9/2.
 */
public class GankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GankAdapter adapter;
    public static final String BASE_URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
    private int page = 1;
    private boolean isFirstEnter = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_gank_fuli);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new GankAdapter(getActivity(),recyclerView,swipeRefreshLayout);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastVisibleItems = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[2]);
                int totalItemCount = staggeredGridLayoutManager.getItemCount();
                int lastVisibleItem = findMax(lastVisibleItems);
                if (lastVisibleItem >= totalItemCount - 4) {
                    sendATask(++page);
                }
            }
        });
        sendATask(1);
        return view;
    }
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    private void sendATask(int page) {
        GsonRequest<GankFuli> gsonRequest = new GsonRequest<>(Request.Method.GET, BASE_URL + page, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, GankFuli.class, new Response.Listener<GankFuli>() {
            @Override
            public void onResponse(GankFuli gankFuli) {
                List<GankResult> gankResults = gankFuli.getResults();
                adapter.addData(gankResults);
                if (isFirstEnter) {
                    isFirstEnter = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        MyApplication.getInstance().add(gsonRequest);
    }

    @Override
    public void onRefresh() {
        if (!isFirstEnter) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
