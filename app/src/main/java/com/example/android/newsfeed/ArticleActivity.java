package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    // Tag for the log messages.
    private static final String LOG_TAG = ArticleActivity.class.getName();
    //JSON response link for a Guardian api query.
    private static final String GUARDIAN_JSON_QUERY = "https://content.guardianapis.com/search?&format=json&from-date=2018-05-12&total=50&show-tags=contributor&page-size=50&api-key=5dfacf98-e445-40d8-b686-1c3ecb66e8f4";
    // TextView that is displayed when the list is empty.
    TextView emptyStateTextView;
    // Variable used to check the network state.
    NetworkInfo networkInfo;
    // Adapter for the list of articles.
    private ArticleAdapter articleAdapter;
    // ProgressBar in the Main Activity.
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the ListView in the layout.
        ListView articleListView = findViewById(R.id.main_activity_listView);
        // Find a reference to the empty state TextView.
        emptyStateTextView = findViewById(R.id.main_activity_empty_state_textView);
        // Set the empty state TextView o the ListView.
        articleListView.setEmptyView(emptyStateTextView);
        // Find a reference to the ProgressBar.
        progressBar = findViewById(R.id.main_activity_progressBar);
        // Create a new adapter that takes an empty list of articles as input.
        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        // Set the adapter on the ListView.
        articleListView.setAdapter(articleAdapter);

        // Check the state of the connection.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Initialise the LoaderManager.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, ArticleActivity.this);
        } else {
            // If there is no network connection hide the ProgressBar and set the "No internet connection available." text on the empty state TextView.
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText("No internet connection available");
        }

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Find the current article that was clicked.
                Article currentArticle = articleAdapter.getItem(position);
                // Create a new intent to view the article URI.
                Intent openArticleWebsite = new Intent(Intent.ACTION_VIEW);
                openArticleWebsite.setData(Uri.parse(currentArticle.getArticleWebsite()));
                startActivity(openArticleWebsite);
            }
        });
    }

    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(ArticleActivity.this, GUARDIAN_JSON_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        // Hide the ProgressBar when the background thread has finished loading the data.
        progressBar.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        emptyStateTextView.setText("No articles found.");
        // Clear the adapter of previous article data.
        articleAdapter.clear();

        // If there is a valid list of articles, then add them to the adapter's data set.
        if (articles != null && !articles.isEmpty()) {
            articleAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        // Loader reset, so we can clear out the existing data.
        articleAdapter.clear();

    }
}