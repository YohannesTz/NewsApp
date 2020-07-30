package com.yohannes.app.dev.newsapp.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yohannes.app.dev.newsapp.R;
import com.yohannes.app.dev.newsapp.ScrollingActivity;

import java.util.List;

/**
 * Created by Yohannes on 03-Apr-20.
 */

public class SavedArticleAdapter extends ArrayAdapter {

    private List<SavedArticle> _savedArticles;

    public SavedArticleAdapter(Context context, List<SavedArticle> savedArticleList) {
        super(context, R.layout.saved_article_row);
        _savedArticles = savedArticleList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.saved_article_row, null, false);

        TextView titleTv = (TextView) customView.findViewById(R.id.savedArticleheader);
        TextView detailTv = (TextView) customView.findViewById(R.id.savedArticleDetail);

        titleTv.setText(_savedArticles.get(position).getTitle());
        detailTv.setText(_savedArticles.get(position).getDetail());

        return customView;
    }
}
