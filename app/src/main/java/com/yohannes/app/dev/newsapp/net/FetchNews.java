package com.yohannes.app.dev.newsapp.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.yohannes.app.dev.newsapp.models.News;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yohannes on 29-Mar-20.
 */

public class FetchNews {
    public List<News> getNews() {
        final List<News> newsList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Util.WEBAPIADRESS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                           /* News fetchedNews = new News(jsonObject.getString("newstitle"), jsonObject.getString("newsdetail"), jsonObject.getString("uploader"));
                            newsList.add(fetchedNews);*/
                        } catch (JSONException jsonex) {
                            jsonex.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                return;
            }
        });
        return newsList;
    }
}
