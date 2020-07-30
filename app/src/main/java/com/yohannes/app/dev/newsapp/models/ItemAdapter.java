package com.yohannes.app.dev.newsapp.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yohannes.app.dev.newsapp.R;
import com.yohannes.app.dev.newsapp.models.News;

/**
 * Created by Yohannes on 09-Mar-20.
 */

public class ItemAdapter extends ArrayAdapter {

    private News[] news;

    public ItemAdapter(@NonNull Context context, News[] news) {
        super(context, R.layout.news_card, news);
        this.news = news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.news_card, null, false);

        TextView newsTitle = (TextView) customView.findViewById(R.id.newstitle);
        TextView newsDetail = (TextView) customView.findViewById(R.id.newsdetail);

        newsTitle.setText(news[position].getNewsHeader());
        newsDetail.setText(news[position].getNewsContent());

        return customView;
    }
}
