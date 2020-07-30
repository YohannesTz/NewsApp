package com.yohannes.app.dev.newsapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.yohannes.app.dev.newsapp.models.User;

import java.sql.ResultSet;

/**
 * Created by Yohannes on 25-Mar-20.
 */

public class DbManager extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "newsApp_users.db";
    private static String TABLE_USERS = "users";
    private User _user;

    private static final String ID_COL = "id";
    private static final String TITLE_COL = "newsTitle";
    private static final String DETAIL_COL = "newsDetail";
    private static final String IMAGE_COL = "image";

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + DATABASE_NAME + "(" +
            ID_COL + " int(11)," +
            TITLE_COL + " TEXT," +
            DETAIL_COL + " MEDIUMTEXT," +
            IMAGE_COL + " BLOB);";

    public DbManager(Context context, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE `users` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `username` text NOT NULL,\n" +
                "  `password` varchar(255) NOT NULL,\n" +
                "  `name` text NOT NULL,\n" +
                "  `fname` text NOT NULL,\n" +
                "  `phonenum` int(11) NOT NULL,\n" +
                "  `bio` text NOT NULL,\n" +
                "  `avatar_link` text NOT NULL\n" +
                ");";

        String create_table = "CREATE TABLE `newsdetail` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `newstitle` text COLLATE utf8_unicode_ci NOT NULL,\n" +
                "  `newsdetail` mediumtext COLLATE utf8_unicode_ci NOT NULL,\n" +
                "  `image` BOLB NOT NULL\n" +
                ");";
        db.execSQL(query);
        //db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //db.execSQL("DROP TABLE IF EXISTS " + );
        onCreate(db);
    }

    public void addUser(User user) {
        if (user != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", user.getId());
            contentValues.put("username", user.getUsername());
            contentValues.put("password", user.getPassword());
            contentValues.put("name", user.getName());
            contentValues.put("fname", user.getFname());
            contentValues.put("phonenum", user.getPhonenum());
            contentValues.put("avatar_link", user.getAvatar_link());
            contentValues.put("bio", user.getBio());

            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_USERS, null, contentValues);
        }
    }

    public User getLastLoggedinUser() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query_getUser = "SELECT * FROM " + TABLE_USERS;

        Cursor c = sqLiteDatabase.rawQuery(query_getUser, null);
        c.moveToFirst();

        //while (!c.isAfterLast()){
        //if (c.getString(c.getColumnIndex("id")) && c.getString(c.getColumnIndex("username")) && c.getString(c.getColumnIndex("password"))&& c.getString(c.getColumnIndex("name"))&& c.getString(c.getColumnIndex("name")) && c.getString(c.getColumnIndex("fname")) && c.getString(c.getColumnIndex("phonenum")) && c.getString(c.getColumnIndex("bio"))&& c.getString(c.getColumnIndex("avatar_link")) != null){

        Log.e("Result Rows count", String.valueOf(c.getCount()));
        if (c.getCount() >= 1) {
            int id = c.getInt(c.getColumnIndex("id"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            String name = c.getString(c.getColumnIndex("name"));
            String fname = c.getString(c.getColumnIndex("fname"));
            int phonenum = c.getInt(c.getColumnIndex("phonenum"));
            String bio = c.getString(c.getColumnIndex("bio"));
            String avatar_link = c.getString(c.getColumnIndex("avatar_link"));

            User user = new User(id, username, password, name, fname, phonenum, bio, avatar_link);
            //Log.e("getLastLoggedinUser", user.toString());
            return user;
        }
        return null;
    }

    public void clearDatabase() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "DELETE FROM `users`;";
        sqLiteDatabase.execSQL(sql);
    }

    public void printData() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query_getUser = "SELECT * FROM " + TABLE_USERS;

        Cursor c = sqLiteDatabase.rawQuery(query_getUser, null);
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex("id"));
        String username = c.getString(c.getColumnIndex("username"));
        String password = c.getString(c.getColumnIndex("password"));
        String name = c.getString(c.getColumnIndex("name"));
        String fname = c.getString(c.getColumnIndex("fname"));
        int phonenum = c.getInt(c.getColumnIndex("phonenum"));
        String bio = c.getString(c.getColumnIndex("bio"));
        String avatar_link = c.getString(c.getColumnIndex("avatar_link"));

        User user = new User(id, username, password, name, fname, phonenum, bio, avatar_link);
        Log.e("printData", user.toString());
        //}
    }

    public void UpdateUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE `users` SET `avatar_link` = '" + user.getAvatar_link() + "' WHERE `username` = " + user.getUsername();

        sqLiteDatabase.execSQL(sql);
        Log.e("UpdateUser", "Sucessfully updated user avatar to " + user.getAvatar_link());
    }

    public void addpost(int id, String title, String detail, byte[] image) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_COL, id);
        cv.put(TITLE_COL, title);
        cv.put(DETAIL_COL, detail);
        cv.put(IMAGE_COL, image);
        database.insert("newsdetail", null, cv);
    }
}
