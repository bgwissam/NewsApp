package com.example.android.newsupdate;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>> {

    //tag for the log message
    private static final String LOG_TAG = NewsLoader.class.getName();

    //Query URL
    private String mUrl;

    /*
    create a constructor for the loader
    load the context
    load the mUrl
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}
