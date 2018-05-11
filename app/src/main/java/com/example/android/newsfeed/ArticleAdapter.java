package com.example.android.newsfeed;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {
    // TODO: add the ArticleAdapter logic.
    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }
}
