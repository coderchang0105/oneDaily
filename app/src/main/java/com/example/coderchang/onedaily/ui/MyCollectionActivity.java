package com.example.coderchang.onedaily.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.coderchang.onedaily.R;
import com.example.coderchang.onedaily.adapter.RVCollectionAdapter;
import com.example.coderchang.onedaily.db.ImportantDatabaseHelper;
import com.example.coderchang.onedaily.doman.SimpleStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/8/25.
 */
public class MyCollectionActivity extends BaseActivity{
    private RecyclerView rvMyCollection;
    private Toolbar toolbar;
    private ImportantDatabaseHelper helper;
    private List<SimpleStory> simpleStoryList = new ArrayList<>();
    private RVCollectionAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intView();
        initToolbar();
        initDatabase();
        initData();
        rvMyCollection.setLayoutManager(new LinearLayoutManager(this));
        rvMyCollection.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVCollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SimpleStory simpleStory) {
                Intent intent = new Intent(MyCollectionActivity.this, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("simpleStory",simpleStory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_collection;
    }

    private void initData() {
        adapter = new RVCollectionAdapter(MyCollectionActivity.this, simpleStoryList);
    }


    private void initDatabase() {
        helper = ImportantDatabaseHelper.getInstance(this, "Collection.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("Story", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SimpleStory simpleStory = new SimpleStory();
                simpleStory.setImage(cursor.getString(cursor.getColumnIndex("image")));
                simpleStory.setStoryId(cursor.getString(cursor.getColumnIndex("storyId")));
                simpleStory.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                simpleStoryList.add(simpleStory);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tb_my_collection);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void intView() {
        rvMyCollection = (RecyclerView) findViewById(R.id.rv_my_collection);
    }
}
