package com.example.android.newsfeed;

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

public class Utilities {

    // Tag for the log messages.
    private static final String LOG_TAG = Utilities.class.getSimpleName();

    private Utilities() {
    }

    // Query the Guardian API and return a list of Article objects.
    public static ArrayList<Article> getArticleData(String requestUrl) {

        // Create URL object.
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Input stream error", e);
        }

        // Extract relevant fields from the JSON response and create an Article object.
        ArrayList<Article> articles = extractFeaturesFromJson(jsonResponse);
        return articles;
    }

    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
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

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Convert the InputStream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = bufferedReader.readLine();
            }
        }
        return streamOutput.toString();
    }

    //Return a list of Article objects that has been built up from parsing a JSON response.
    private static ArrayList<Article> extractFeaturesFromJson(String articleJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJson)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding articles to.
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the USGS_JSON_QUERY. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Convert GUARDIAN_JSON_QUERY String into a JSONObject.
            JSONObject baseJsonObject = new JSONObject(articleJson);
            // Extract “response” JSONArray.
            JSONArray articleArray = baseJsonObject.getJSONArray("response");

            // Loop through each feature in the array.
            for (int i = 0; i < articleArray.length(); i++) {
                // Get earthquake JSONObject at position i.
                JSONObject currentArticle = articleArray.getJSONObject(i);
                // For a given article, extract the JSONObject associated with the
                // key called "results", which represents a list of all properties
                // for that article.
                JSONObject articleProperties = currentArticle.getJSONObject("results");

                // Extract “sectionName” for the name of the section.
                String section = articleProperties.getString("sectionName");
                // Extract “webTitle” for the title of the article.
                String title = articleProperties.getString("webTitle");
                // Extract “webPublicationDate” for the date of the article.
                String date = articleProperties.getString("webPublicationDate");
                // Extract "references" for the author of the article.
                String author = articleProperties.getString("references");
                // Extract "webUrl" for the webpage of the article.
                String webpage = articleProperties.getString("webUrl");

                // Create Article java object from section, title, date, author, webpage.
                Article article = new Article(section, title, date, author, webpage);
                // Add article to list of articles.
                articles.add(article);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }
        // Return the list of articles.
        return articles;
    }
}
