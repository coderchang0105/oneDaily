package com.example.coderchang.onedaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by coderchang on 16/8/25.
 */
public class ImportantDatabaseHelper extends SQLiteOpenHelper{
    private Context mContext;
    public static final String CREATE_STORY = "create table Story ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "image text, "
            + "storyId text)";

    public ImportantDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORY);
        Toast.makeText(mContext, "Created database success!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
