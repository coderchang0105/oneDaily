package com.example.coderchang.onedaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.doman.SimpleStory;
import com.example.coderchang.onedaily.utils.NetUtil;

import java.util.List;

/**
 * Created by coderchang on 16/8/25.
 */
public class RVCollectionAdapter extends RecyclerView.Adapter{
    private List<SimpleStory> mData;
    private Context mContext;
    private RVCollectionAdapter.OnItemClickListener mListener;
    public RVCollectionAdapter(Context mContext, List<SimpleStory> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_main_item, parent, false);
        CollectionViewHolder holder = new CollectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SimpleStory simpleStory = mData.get(position);
        final CollectionViewHolder viewHolder = (CollectionViewHolder) holder;
        viewHolder.textView.setText(simpleStory.getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(simpleStory);
            }
        });
        final Handler handler = new Handler();
        NetUtil.asyncImageGet(simpleStory.getImage(), new NetUtil.ImageCallback() {
            @Override
            public void onSuccess(final Bitmap bitmap) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.imageView.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(mContext, "获取ivRvItemPic失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder{

        public CollectionViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_rv_item_title);
            imageView = (ImageView) itemView.findViewById(R.id.iv_rv_item_pic);
        }

        TextView textView;
        ImageView imageView;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
    public static interface OnItemClickListener{
        void onItemClick(SimpleStory simpleStory);
    }
}
