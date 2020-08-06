package com.yohannes.app.dev.newsapp.util;

import android.util.Log;

import com.yohannes.app.dev.newsapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yohannes on 24-Mar-20.
 */

public class Util {
    public static String NEWS_DETAILAPI = "https://addisnews.000webhostapp.com/news/getdetail.php";
    public static String WEBAPIADRESS = "https://addisnews.000webhostapp.com/news/index.php";
    public static String UPLOADIMAGEADRESS = "https://addisnews.000webhostapp.com/news/upload.php";
    public static int CONNECTION_TIMEOUT = 20000; //20 seconds
    public static int READ_TIMEOUT = 20000;
    public static String ENCODING = "UTF-8";
    //public static String WEBAPIADRESS = "http://192.168.137.1/news/index.php";
    //public static String NEWS_DETAILAPI = "http://192.168.137.1/news/getDetail.php";

    public static boolean isJsonvalid(String jsoninString) {
        try {
            new JSONObject(jsoninString);
            Log.e("isJsonInvalid", "jsonInString is JSONObject");
        } catch (JSONException ex) {
            Log.e("jsoninvalid", "JsonObject is invalid!");
            try {
                new JSONArray(jsoninString);
                Log.e("isJsonInvalid", "jsonInString is JSONArray");
            } catch (JSONException ex1) {
                Log.e("jsoninvalid", "JsonArray is invalid!");
                return false;
            }
        }
        return true;
    }

    public User loggedInUser(String serverresult) {
        if (Util.isJsonvalid(serverresult)) {
            try {
                JSONObject jsonObject = new JSONObject(serverresult);
                User loggedInUser = new User(jsonObject.getInt("id"), jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("name"), jsonObject.getString("fname"), jsonObject.getInt("phonenum"), jsonObject.getString("bio"), jsonObject.getString("avatar_link"));
                Log.e("loggedInUser", loggedInUser.toString());
                return loggedInUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
