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
import com.example.coderchang.onedaily.doman.SimpleStory;

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
        ImageRequest imageRequest = new ImageRequest(simpleStory.getImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                viewHolder.imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getInstance().add(imageRequest);
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
