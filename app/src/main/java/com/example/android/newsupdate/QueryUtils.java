package com.example.android.newsupdate;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//this method will request and receive News data from the API - Guardians
public final class QueryUtils {
    //tag log for the QueryUtils
    private static final String LOG_TAG = QueryUtils.class.getName();
    //stores the key variable to extract from JSON object
    private static final String response = "response";
    //stores the key varialbe for extracting from the JSON array
    private static final String results = "results";
    //create private constructor
    public QueryUtils() {
    }

    //Query Guardians data and return a list of objects
    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createURL(requestUrl);
        //perform HTTP request to the Guardians and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in making the HTTP request " + e);
        }
        //extract relevent fields from the JSON response
        List<News> news = extractResponseFromJSON(jsonResponse);

        return news;
    }

    //returns new URL object from given url
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Couldn't build URL " + e);
        }
        return url;
    }

    //make HTTp request and return string as the response
    private static String makeHTTpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //check request for errors
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON result " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    //Convert the InputStream into the String which contains the whole JSON response from the server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractResponseFromJSON(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            Log.e(LOG_TAG, "newJSON is empty");
            return null;
        }
        //create empty arraylist to start adding news to
        List<News> news = new ArrayList<>();

        try {
            //Create a JSONobject from JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            //extract JSONArray
            JSONObject newsObject = baseJsonResponse.getJSONObject(response);
            //get the results array
            JSONArray newsArray = newsObject.getJSONArray(results);

            for (int i = 0; i < newsArray.length(); i++) {

                //get a single news at position i within the list of arrays
                JSONObject currentNews = newsArray.getJSONObject(i);

                //get the section name
                String section = currentNews.getString("sectionName");
                //get the title
                String title = currentNews.getString("webTitle");
                //get the url
                String date = currentNews.getString("webPublicationDate");
                //get the date
                String url = currentNews.getString("webUrl");
                //get author data from tags
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                JSONObject tagsObject = tagsArray.getJSONObject(0);
                //get first name and last name
                String firstName = tagsObject.getString("firstName");
                String lastName = tagsObject.getString("lastName");

                News news1 = new News(section, title, date, url, firstName, lastName);
                //add the news to the list
                news.add(news1);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem passing the NEWs JSON results " + e);
        }
        return news;
    }
}


