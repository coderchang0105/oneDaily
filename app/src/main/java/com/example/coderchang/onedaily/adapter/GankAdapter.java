package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.GankResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/9/3.
 */
public class GankAdapter extends RecyclerView.Adapter {

    private List<GankResult> gankResultList;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private LruCache<String, Bitmap> mMemoryCache;

    public GankAdapter(Context context, RecyclerView recyclerView, SwipeRefreshLayout refreshLayout) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        mRefreshLayout = refreshLayout;
        gankResultList = new ArrayList<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        mMemoryCache = new LruCache<String,Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addToMemoryCache(String url, Bitmap bitmap) {
        if (getFromMemoryCache(url) == null) {
            mMemoryCache.put(url, bitmap);
        }
    }

    public Bitmap getFromMemoryCache(String url) {
        return mMemoryCache.get(url);
    }


    public void addData(List<GankResult> data) {
        if (data != null) {
            gankResultList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_gank_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GankResult gankResult = gankResultList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;
        ViewGroup.LayoutParams lp = viewHolder.imageView.getLayoutParams();
        lp.height = (int) (Math.random() * 200 + 500);
        viewHolder.imageView.setLayoutParams(lp);
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        viewHolder.imageView.setTag(gankResult.getUrl());

        loadBitmap(viewHolder.imageView, gankResult.getUrl());



    }

    private void loadBitmap(final ImageView imageView, final String url) {
        Bitmap bitmap = getFromMemoryCache(url);
        if (bitmap == null) {
            final ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if (url.equals(imageView.getTag())) {
                        imageView.setImageBitmap(bitmap);
                        addToMemoryCache(url,bitmap);
                    }
                }
            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MyApplication.getInstance().add(imageRequest);
        } else {
            if (url.equals(imageView.getTag())) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return gankResultList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_gank_item);
        }

        ImageView imageView;
    }

}
