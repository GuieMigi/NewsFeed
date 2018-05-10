package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {
    // Tag for log messages.
    private static final String LOG_TAG = ArticleLoader.class.getName();
    // Query URLs.
    private String[] mUrls;

    public ArticleLoader(Context context, String... urls) {
        super(context);
        mUrls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrls.length < 1 || mUrls[0] == null) {
            return null;
        }
        // Perform the HTTP request for article data and process the response.
        ArrayList<Article> earthquakes = Utilities.getArticleData(mUrls[0]);
        return earthquakes;
    }
}
