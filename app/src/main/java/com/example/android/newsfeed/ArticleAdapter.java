package com.example.android.newsfeed;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Find the article at the given position in the list of articles.
        Article currentArticle = getItem(position);

        // Find the TextView with view ID list_item_section_name_textView.
        TextView sectionNameTextView = convertView.findViewById(R.id.list_item_section_name_textView);
        // Display the section of the current article in the TextView.
        sectionNameTextView.setText(currentArticle.getArticleSectionName());

        // Find the TextView with view ID list_item_article_title_textView.
        TextView titleTextView = convertView.findViewById(R.id.list_item_article_title_textView);
        // Display the title of the current article in that TextView.
        titleTextView.setText(currentArticle.getArticleTitle());

        // Split the dateTime string into date and time Strings.
        String dateAndTime = currentArticle.getArticleDate();
        String date;
        String unformattedTime;
        String time;
        String[] dateAndTimeParts = dateAndTime.split("T");
        date = dateAndTimeParts[0];
        unformattedTime = dateAndTimeParts[1];
        String[] timeParts = unformattedTime.split("Z");
        time = timeParts[0];

        // Find the TextView with view ID list_item_article_date_textView.
        TextView dateTextView = convertView.findViewById(R.id.list_item_article_date_textView);
        // Display the date of the current article in the TextView
        dateTextView.setText(date);

        // Find the TextView with view ID list_item_article_time_textView.
        TextView timeTextView = convertView.findViewById(R.id.list_item_article_time_textView);
        // Display the date of the current article in the TextView.
        timeTextView.setText(time);

        // Find the TextView with view ID list_item_article_author_name_textView.
        TextView authorTextView = convertView.findViewById(R.id.list_item_article_author_name_textView);
        // If there is no author name then hide the TextView.
        if (currentArticle.getArticleAuthor() == null) {
            authorTextView.setVisibility(View.GONE);
            // Display the author of the current article in the TextView.
        } else authorTextView.setText(currentArticle.getArticleAuthor());
        return convertView;
    }
}