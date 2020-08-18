package com.yohannes.app.dev.newsapp.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yohannes.app.dev.newsapp.R;
import com.yohannes.app.dev.newsapp.ScrollingActivity;

import java.util.List;


/**
 * Created by Yohannes on 26-Mar-20.
 */

public class NewsListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<News> newsList;
    private Context context = null;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public NewsListAdapter(List<News> _newsList, RecyclerView recyclerView) {
        this.newsList = _newsList;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // The End Has been reached
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return newsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
            vh = new NewsViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            News singleNews = newsList.get(position);
            String trimmedContent = String.valueOf(singleNews.getNewsContent()).trim();
            ((NewsViewHolder) holder).newsTitle.setText(singleNews.getNewsHeader());
            ((NewsViewHolder) holder).newsDetail.setText(singleNews.getNewsContent());
            ((NewsViewHolder) holder).uploader.setText(singleNews.getUploader());
            ((NewsViewHolder) holder).date.setText(singleNews.getDate());
            //((NewsViewHolder) holder).viewtv.setText(singleNews.getView());
            ((NewsViewHolder) holder).newsInside = singleNews;
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    private class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView newsTitle;
        private TextView newsDetail;
        private TextView uploader;
        private TextView date;
        private TextView viewtv;

        private News newsInside;

        private NewsViewHolder(View v) {
            super(v);
            newsTitle = v.findViewById(R.id.newstitle);
            newsDetail = v.findViewById(R.id.newsdetail);
            uploader = v.findViewById(R.id.newsUploader);
            date = v.findViewById(R.id.newsDate);
            viewtv = v.findViewById(R.id.newsViews);

            context = v.getContext();

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailView = new Intent(context, ScrollingActivity.class);
                    detailView.putExtra("News", newsInside);
                    context.startActivity(detailView);
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }
}
