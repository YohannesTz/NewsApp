package com.yohannes.app.dev.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.yohannes.app.dev.newsapp.models.NewsListAdapter;
import com.yohannes.app.dev.newsapp.models.SavedArticle;
import com.yohannes.app.dev.newsapp.models.SavedArticleAdapter;
import com.yohannes.app.dev.newsapp.util.SavedPostManager;

import java.util.List;

public class SavedArticles extends AppCompatActivity {

    private List<SavedArticle> _savedArticles;
    private ListView savedArticlesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);

        savedArticlesListView = findViewById(R.id.saved_list);

        SavedPostManager savedPostManager = new SavedPostManager(this, null, 1);
        _savedArticles = savedPostManager.getSavedArticles();
//        Log.e("savedArticlesSize", String.valueOf(_savedArticles.size()));

        savedArticlesListView.setAdapter(new SavedArticleAdapter(this, _savedArticles));

    }
}
