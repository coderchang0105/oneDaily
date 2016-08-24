package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.utils.NetUtil;

import java.util.List;

import javax.net.ssl.HandshakeCompletedEvent;

/**
 * Created by coderchang on 16/8/23.
 */
public class RVMainAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Story> mData;
    private View mCarouselView;
    public static final int TYPE_CAROUSEL = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;

    private OnLoadListener mListener;
    public RVMainAdapter(Context context, List<Story> data) {
        this.mContext = context;
        this.mData = data;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CarouselViewHolder) {
            CarouselViewHolder viewHolder = (CarouselViewHolder) holder;
        }
        if (holder instanceof NormalViewHolder) {
            final Handler handler = new Handler();
            Story story = mData.get(position-1);
            final NormalViewHolder viewHolder = (NormalViewHolder) holder;
            viewHolder.tvRVItemTitle.setText(story.getTitle());
            NetUtil.asyncImageGet(story.getImages().get(0), new NetUtil.ImageCallback() {
                @Override
                public void onSuccess(final Bitmap bitmap) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.ivRVItemPic.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void onFail(Exception e) {
                    Toast.makeText(mContext, "获取ivRvItemPic失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (holder instanceof BootViewHolder) {
            BootViewHolder viewHolder = (BootViewHolder) holder;
            Log.d("TAG", "加载数据.....");
            mListener.onLoad();
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
        void onLoad();
    }
}
