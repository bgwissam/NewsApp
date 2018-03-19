package com.example.android.newsupdate;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    /*
    Variable that will be used along the activity
    @Param: LOG_TAG to tag all log messages
            GUARDIAN_REQUEST_URL will be the url address from which data is imported
            mAdapter holds that value of the new data list
            NEWS_LOADER_ID = 1 hold the value for the loader current activity
     */
    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar mIndifiniteProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //set empty state textview
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        //set progress bar state
        mIndifiniteProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        //find the reference for the list view in the layout
        final ListView newsListView = findViewById(R.id.list);
        //create a new adapter which take an empty list of news
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        newsListView.setEmptyView(mEmptyStateTextView);

        //Check the connectivity in the below section
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingInicator = findViewById(R.id.loading_spinner);
            loadingInicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_conn);
        }
        //create an onClick listener to open the webURL for each news thread
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the earthquake that was clicked
                News currentNews = mAdapter.getItem(position);
                //Convert the string Url to a uri object to pass into the internet constructor
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                //Create a new intent to view the curent earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //Launch intent
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "OnCreate Load was activiated");
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //if no NEWs  was found
        mEmptyStateTextView.setText(R.string.no_news_found);
        mAdapter.clear();
        //if there are news on the page
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            mIndifiniteProgressBar.setVisibility(View.GONE);
            Log.i(LOG_TAG, "OnLoadFinished was activited");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
        Log.i(LOG_TAG, "OnLoaderReset was activited");
    }
}
