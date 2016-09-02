package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.TopStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/8/24.
 */
public class CarouselAdapter extends PagerAdapter{

    private List<TopStory> mData = new ArrayList<>();
    private Context mContext;
    private List<View> views;

    private OnItemClickListener mListener;
    public CarouselAdapter(Context context) {
        this.mContext = context;
        views = new ArrayList<>();
    }

    public void addData(List<TopStory> topStories) {
        if (topStories != null) {
            mData.addAll(topStories);
        }
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.carousel_main_item,null);
        TopStory topStory = mData.get(position);
        TextView textView = (TextView) view.findViewById(R.id.tv_carousel_title);
        textView.setText(mData.get(position).getTitle());
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_carousel_pic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mData.get(position));
            }
        });
        ImageRequest imageRequest = new ImageRequest(topStory.getImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getInstance().add(imageRequest);
        views.add(view);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public interface OnItemClickListener{
        void onItemClick(TopStory topStory);
    }

    public List<TopStory> getData() {
        return mData;
    }
}
