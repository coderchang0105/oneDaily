package com.example.coderchang.onedaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coderchang.onedaily.doman.SimpleStory;
import com.example.coderchang.onedaily.doman.Story;
import com.example.coderchang.onedaily.doman.TopStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderchang on 16/9/2.
 */
public class CollectionDB {
    public static final String DB_NAME = "zhihuCollection";
    private ImportantDatabaseHelper helper;
    private SQLiteDatabase db;
    private static CollectionDB singleton;
    private Context mContext;

    private CollectionDB(Context context) {
        this.mContext = context;
        helper = new ImportantDatabaseHelper(context, DB_NAME, null, 1);
        db = helper.getWritableDatabase();
    }

    public static CollectionDB getInstance(Context context) {
        if (singleton == null) {
            synchronized (CollectionDB.class) {
                if (singleton == null) {
                    singleton = new CollectionDB(context);

                }
            }
        }
        return singleton;
    }

    public <T> void saveSimpleStory(T t) {
        if (t instanceof Story){
            Story story = (Story) t;
            if (story != null) {
                ContentValues values = new ContentValues();
                values.put("title", story.getTitle());
                values.put("image", story.getImages().get(0));
                values.put("storyId", story.getId());
                db.insert("Story", null, values);
                values.clear();
            }
        }
        if (t instanceof TopStory) {
            TopStory topstory = (TopStory) t;
            if (topstory != null) {
                ContentValues values = new ContentValues();
                values.put("title", topstory.getTitle());
                values.put("image", topstory.getImage());
                values.put("storyId", topstory.getId());
                db.insert("Story", null, values);
                values.clear();
            }
        }



    }

    public <T> boolean isFavorite(T t) {
        if (t instanceof Story) {
            Story story = (Story) t;
            Cursor cursor = db.query("Story", null, "storyId = ?", new String[]{story.getId() + ""}, null, null, null);
            if (cursor.moveToNext()) {
                return true;
            } else {
                return false;
            }
        }

        if (t instanceof TopStory) {
            TopStory topStory = (TopStory) t;
            Cursor cursor = db.query("Story", null, "storyId = ?", new String[]{topStory.getId() + ""}, null, null, null);
            if (cursor.moveToNext()) {
                return true;
            } else {
                return false;
            }
        }

        if (t instanceof SimpleStory) {
            SimpleStory simpleStory = (SimpleStory) t;
            Cursor cursor = db.query("Story", null, "storyId = ?", new String[]{simpleStory.getStoryId() + ""}, null, null, null);
            if (cursor.moveToNext()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public List<SimpleStory> loadSimpleStoryList() {
        List<SimpleStory> simpleStoryList = new ArrayList<>();
        Cursor cursor = db.query("Story", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SimpleStory simpleStory = new SimpleStory();
                simpleStory.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                simpleStory.setImage(cursor.getString(cursor.getColumnIndex("image")));
                simpleStory.setStoryId(cursor.getString(cursor.getColumnIndex("storyId")));
                simpleStoryList.add(simpleStory);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return simpleStoryList;
    }
}
