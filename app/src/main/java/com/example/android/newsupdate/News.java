package com.example.android.newsupdate;


public class News {

    //web title to illustrate the news discription
    private String mWebTitle;
    //will present the publication date
    private String mDate;
    //url to present a link a for the news
    private String mUrl;
    //Section name
    private String mSection;

    //Create a constructor
    public News(String section, String webTitle, String date, String url) {
        mSection = section;
        mWebTitle = webTitle;
        mDate = date;
        mUrl = url;
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
}
