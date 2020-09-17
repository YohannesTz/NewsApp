package com.yohannes.app.dev.newsapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yohannes.app.dev.newsapp.models.News;
import com.yohannes.app.dev.newsapp.models.NewsDetail;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Articlemedium extends AppCompatActivity {

    private News news;
    private NewsDetail newsDetail = null;

    private TextView newsDetailtv;
    private TextView titletv;
    private ImageView scrollImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articlemedium);
        final Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        news = (News) getIntent().getSerializableExtra("News");
        //Fetch news Detail
        if (news != null) {
            getNewsDetail(news);
        }
        titletv = findViewById(R.id.titleText2);
        newsDetailtv = findViewById(R.id.largeText2);
        scrollImageView = findViewById(R.id.scrollImageView2);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout2);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpanededAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbar.setTitle("");

        if (newsDetail != null) {
            configureWidgets();
        }
    }

    private void getNewsDetail(News n) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Util.NEWS_DETAILAPI + "?id=" + n.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("newsDetail", jsonObject.toString());
                    newsDetail = new NewsDetail(jsonObject.getInt("id"), jsonObject.getString("newstitle"), jsonObject.getString("newsdetail"), jsonObject.getString("image"));
                    configureWidgets();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", volleyError.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void configureWidgets() {
        titletv.setText(newsDetail.getNewsTitle());
        newsDetailtv.setText("\n" + newsDetail.getNewsDetail() + "\n" + "\n");
        Picasso.with(this).load(newsDetail.getImageUrl()).into(scrollImageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) scrollImageView.getDrawable()).getBitmap();
            }

            @Override
            public void onError() {
                //// TODO: 8/6/2020 Add an error catching method
            }
        });
    }
}
