package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
import com.example.coderchang.onedaily.doman.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/8/23.
 */
public class RVMainAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Story> mData = new ArrayList<>();
    private View mCarouselView;
    private String mDate;
    public static final int TYPE_CAROUSEL = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;

    private OnLoadListener mListener;

    private OnItemClickListener clickListener;

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    public RVMainAdapter(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public void addData(List<Story> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mListener = listener;
    }
    public void setCarouselView(View carouselView) {
        this.mCarouselView = carouselView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CAROUSEL;
        } else if (position == mData.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAROUSEL) {
            CarouselViewHolder viewHolder = new CarouselViewHolder(mCarouselView);
            return viewHolder;
        }
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_main_item, parent, false);
            NormalViewHolder viewHolder = new NormalViewHolder(view);
            return viewHolder;
        }
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rv_main_booter, parent, false);
            BootViewHolder viewHolder = new BootViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CarouselViewHolder) {
            CarouselViewHolder viewHolder = (CarouselViewHolder) holder;
        }
        if (holder instanceof NormalViewHolder) {
            final Story story = mData.get(position-1);
            final NormalViewHolder viewHolder = (NormalViewHolder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(story);

                }
            });
            viewHolder.tvRVItemTitle.setText(story.getTitle());
            String url = story.getImages().get(0);
            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    viewHolder.ivRVItemPic.setImageBitmap(bitmap);
                }
            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MyApplication.getInstance().add(imageRequest);
        }
        if (holder instanceof BootViewHolder) {
            BootViewHolder viewHolder = (BootViewHolder) holder;
            if (mListener != null) {
                mListener.onLoad(mDate);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size() + 2;
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
            tvRVItemTitle = (TextView) itemView.findViewById(R.id.tv_rv_item_title);
            ivRVItemPic = (ImageView) itemView.findViewById(R.id.iv_rv_item_pic);
        }

        TextView tvRVItemTitle;
        ImageView ivRVItemPic;
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {

        public CarouselViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BootViewHolder extends RecyclerView.ViewHolder {
        public BootViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnLoadListener{
        void onLoad(String date);
    }

    public interface OnItemClickListener{
        void onItemClick(Story story);
    }
}
