package com.example.android.newsupdate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.android.newsupdate.R.color.colorAccent;


public class NewsAdapter extends ArrayAdapter<News> {

    private static final String FULLTIME_SEPERATOR = "T";
    private static final String DATE_SEPERATOR = "-";
    private static final String TIME_SEPERATOR = ":";

    //will create a default constructor
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    //returns a list of items the displays the information about the news gathered from the API
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //check if there's an existing list of items, otherwise inflate a new list
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_items, parent, false);
        }
        News currentNews = getItem(position);
        //will be used to grap the time publication value and split it according to date and time
        String dateVariable = "";
        String timeVariable = "";

        String timePublication = currentNews.getmDate();
        if (timePublication.contains(FULLTIME_SEPERATOR)) {
            //split the String so that it shows time from Date
            String[] parts = timePublication.split(FULLTIME_SEPERATOR);
            dateVariable = parts[0];
            timeVariable = parts[1];
        } else {
            dateVariable = timePublication;
            timeVariable = "";
        }
        //set the date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formatDate = formatDate(dateVariable);
        dateView.setText(formatDate);

        //set the time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formatTime = formatTime(timeVariable);
        timeView.setText(formatTime);

        //Find the type of news
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_view);
        sectionView.setText(currentNews.getmSection());

        GradientDrawable sectionViewBackground = (GradientDrawable) sectionView.getBackground();
        int sectionColor = getSectionColor(currentNews.getmSection());
        sectionViewBackground.setColor(sectionColor);

        //find webtitle view and set in its proper place
        TextView webTitleView = (TextView) listItemView.findViewById(R.id.webtitle_view);
        webTitleView.setText(currentNews.getmWebTitle());

        return listItemView;
    }

    public String formatDate(String dateObject) {
        String date = null;
        if (dateObject.contains(DATE_SEPERATOR)) {
            String[] parts = dateObject.split(DATE_SEPERATOR);
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];

            date = getMonthSymbol(Integer.valueOf(month)) + " " + day + ", " + year;
        }
        return date;
    }

    public String getMonthSymbol(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public String formatTime(String timeObject) {
        String time = null;
        if (timeObject.contains(TIME_SEPERATOR)) {
            String[] parts = timeObject.split(TIME_SEPERATOR);
            String hour = parts[0];
            String minutes = parts[1];

            time = hour + ":" + minutes;
        }
        return time;
    }

    public int getSectionColor(String section) {
        int sectionColorId = 0;
        switch (section.toLowerCase()) {
            case "politics":
                sectionColorId = R.color.politics;
                break;
            case "business":
                sectionColorId = R.color.business;
                break;
            case "world news":
                sectionColorId = R.color.worldNews;
                break;
            case "opinion":
                sectionColorId = R.color.opinion;
                break;
            default:
                sectionColorId = R.color.notfound;
        }
        return ContextCompat.getColor(getContext(), sectionColorId);
    }
}
