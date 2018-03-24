package com.example.android.newsupdate;


import android.util.Log;

public class News {

    //web title to illustrate the news discription
    private String mWebTitle;
    //will present the publication date
    private String mDate;
    //url to present a link a for the news
    private String mUrl;
    //Section name
    private String mSection;
    //author first and last name
    private String mFirst;
    private String mLast;

    //Create a constructor
    public News(String section, String webTitle, String date, String url, String first, String last) {
        mSection = section;
        mWebTitle = webTitle;
        mDate = date;
        mUrl = url;
        mFirst = first;
        mLast = last;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getAuthorName () {
        String authorName = null;
        authorName = mFirst + " " + mLast;
        return authorName;
    }
}
