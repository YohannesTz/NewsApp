package com.yohannes.app.dev.newsapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.ICUUncheckedIOException;
import android.util.Log;
import android.widget.Toast;

import com.yohannes.app.dev.newsapp.models.NewsDetail;
import com.yohannes.app.dev.newsapp.models.SavedArticle;
import com.yohannes.app.dev.newsapp.models.User;

import java.util.List;

/**
 * Created by Yohannes on 03-Apr-20.
 */

public class SavedPostManager extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "newsAppsavedPosts.db";
    private static String TABLE_NEWS = "news";
    private NewsDetail _newsDetail;

    private static final String NEWSDETAIL_TABLE = "newsdetail";
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "newsTitle";
    private static final String DETAIL_COL = "newsDetail";
    private static final String IMAGE_COL = "image";

    private Context _context;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + NEWSDETAIL_TABLE + "(" +
            ID_COL + " int(11)," +
            TITLE_COL + " TEXT," +
            DETAIL_COL + " MEDIUMTEXT," +
            IMAGE_COL + " BLOB);";

    public SavedPostManager(Context context, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(db);
    }

    public void addpost(int id, String title, String detail, byte[] image) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_COL, id);
        cv.put(TITLE_COL, title);
        cv.put(DETAIL_COL, detail);
        cv.put(IMAGE_COL, image);
        database.insert(TABLE_NEWS, null, cv);

        /*String add_post = "INSERT INTO "+ TABLE_NEWS + " (`id`, `newsTitle`, `newsDetail`, `images`) VALUES ("
                + id + ", " + title + ", " + detail + ", " + image + ");";
        Log.e("SQL", add_post);
        database.execSQL(add_post);*/

        Toast.makeText(_context, "Sucessfully Added", Toast.LENGTH_SHORT).show();
    }

    public List<SavedArticle> getSavedArticles() {
        List<SavedArticle> savedArticles = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * from " + NEWSDETAIL_TABLE, null);
        result.moveToFirst();
        Log.e("Dbsize", String.valueOf(result.getCount()));
        if (result.getCount() >= 1) {
            while (!result.isAfterLast()) {
                byte[] image = result.getBlob(result.getColumnIndex(IMAGE_COL));
                SavedArticle fetchedArticle = new SavedArticle(result.getInt(result.getColumnIndex(ID_COL)), result.getString(result.getColumnIndex(TITLE_COL)), result.getString(result.getColumnIndex(DETAIL_COL)), image);
                savedArticles.add(fetchedArticle);
                Log.e("Added", "Added the Article");
            }
        }
        return savedArticles;
    }
}
