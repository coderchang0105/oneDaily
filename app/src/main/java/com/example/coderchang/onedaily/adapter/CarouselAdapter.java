package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.TopStory;
import com.example.coderchang.onedaily.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/8/24.
 */
public class CarouselAdapter extends PagerAdapter{

    private List<TopStory> mData;
    private Context mContext;
    private List<View> views;

    private OnItemClickListener mListener;
    public CarouselAdapter(Context context,List<TopStory> data) {
        this.mData = data;
        this.mContext = context;
        views = new ArrayList<>();
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

        final Handler handler = new Handler();
        View view = LayoutInflater.from(mContext).inflate(R.layout.carousel_main_item,null);

        TextView textView = (TextView) view.findViewById(R.id.tv_carousel_title);
        textView.setText(mData.get(position).getTitle());
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_carousel_pic);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mData.get(position));
            }
        });

        NetUtil.asyncImageGet(mData.get(position).getImage(), new NetUtil.ImageCallback() {
            @Override
            public void onSuccess(final Bitmap bitmap) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onFail(Exception e) {

            }
        });
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
}
