package com.yohannes.app.dev.newsapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yohannes.app.dev.newsapp.models.News;
import com.yohannes.app.dev.newsapp.models.NewsDetail;
import com.yohannes.app.dev.newsapp.util.ImageUtilty;
import com.yohannes.app.dev.newsapp.util.SavedPostManager;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class ScrollingActivity extends AppCompatActivity {

    private News news;
    private NewsDetail newsDetail = null;

    private TextView newsDetailtv;
    private TextView titletv;
    private ImageView scrollImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        news = (News) getIntent().getSerializableExtra("News");
        //Fetch news Detail
        getNewsDetail(news);

        titletv = (TextView) findViewById(R.id.titleText);
        newsDetailtv = (TextView) findViewById(R.id.largeText);
        scrollImageView = (ImageView) findViewById(R.id.scrollImageView);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(null);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpanededAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbar.setTitle(null);

        if (newsDetail != null) {
            configureWidgets();
        }
    }

    private void savepost(NewsDetail newsDetail) {
        SavedPostManager spm = new SavedPostManager(this, null, 1);
        Bitmap bitmap = ((BitmapDrawable) scrollImageView.getDrawable()).getBitmap();
        if (bitmap != null) {
            spm.addpost(newsDetail.getId(), newsDetail.getNewsTitle(), newsDetail.getNewsDetail(), ImageUtilty.getBytes(bitmap));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
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

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menu, menu);
        return true;
    }*/
}
