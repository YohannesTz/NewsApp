package com.yohannes.app.dev.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.yohannes.app.dev.newsapp.models.News;
import com.yohannes.app.dev.newsapp.models.NewsListAdapter;
import com.yohannes.app.dev.newsapp.models.OnLoadMoreListener;
import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.CircleAvatar;
import com.yohannes.app.dev.newsapp.util.DbManager;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsListAdapter newsAdapter;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private DbManager dbManager;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;

    //definig widgets
    private TextView userNameText;
    private TextView nameText;
    private ImageView userAvatarimage;
    private TextView emptyText;

    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rq = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.newsRecyclerList);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);

        userNameText = header_view.findViewById(R.id.usernameText);
        nameText = header_view.findViewById(R.id.nameText);
        userAvatarimage = header_view.findViewById(R.id.userAvatarImage);
        emptyText = new TextView(this);
        emptyText.setText("Ooops! No news today!");
        emptyText.setVisibility(View.GONE);
        setupData();

        loadFirstData();

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsListAdapter(newsArrayList, recyclerView);
        recyclerView.setAdapter(newsAdapter);

        if (newsArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        newsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                newsArrayList.add(null);
                newsAdapter.notifyItemInserted(newsArrayList.size());
                getNews();
            }
        });
    }


    private void loadFirstData() {
        News newsFirst = (News) getIntent().getSerializableExtra("InitialData");
        Log.e("FirstData", newsFirst.toString());
        newsArrayList.add((News) getIntent().getSerializableExtra("InitialData"));
        //getNews();
        //newsAdapter.notifyItemInserted(newsArrayList.size());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            dbManager = new DbManager(this, null, 1);
            dbManager.clearDatabase();
            gotoLoginActivity();

        } else if (id == R.id.nav_settings) {
            gotoSettings();
        } else if (id == R.id.nav_scroll) {
            gotoScroll();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void gotoLoginActivity() {
        Intent registerIntent = new Intent(this, LoginActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void gotoSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void gotoScroll() {
        Intent intent = new Intent(this, SavedArticles.class);
        startActivity(intent);
    }


    private void setupData() {
        dbManager = new DbManager(this, null, 1);
        User loggedInUser = dbManager.getLastLoggedinUser();
        Log.e("FetchedfromDatabase", loggedInUser.toString());
        if (loggedInUser != null) {
            userNameText.setText(loggedInUser.getUsername());
            nameText.setText(loggedInUser.getName() + " " + loggedInUser.getFname());
            Picasso.with(MainActivity.this).load(loggedInUser.getAvatar_link()).transform(new CircleAvatar()).into(userAvatarimage);
        }
    }

    private void getNews() {
        System.out.println("getNew was Called");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Util.WEBAPIADRESS, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("JsonArrayRequest", jsonArray.toString());
                Log.e("JsonArraySize", String.valueOf(jsonArray.length()));
                //if (newsArrayList.size() > 0) {
                    newsArrayList.remove(newsArrayList.size() - 1);
                newsAdapter.notifyItemRemoved(newsArrayList.size());
                //}
                for (int i = 0; i < jsonArray.length(); i++) {

                    News fnews = new News();
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        fnews = new News(jsonObject.getInt("id"), jsonObject.getString("uploader"), jsonObject.getString("newsHeader"), jsonObject.getString("newsContent"), jsonObject.getString("detailImage"), jsonObject.getString("date"), jsonObject.getInt("view"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    newsArrayList.add(fnews);
                    newsAdapter.notifyItemInserted(newsArrayList.size());
                    newsAdapter.setLoaded();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Volley Error", volleyError.toString());
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonArrayRequest);
    }

}
