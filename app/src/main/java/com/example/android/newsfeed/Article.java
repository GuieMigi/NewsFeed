package com.example.android.newsfeed;

public class Article {
    // The section name of the article.
    private String mSection;
    // The title of the article.
    private String mTitle;
    // The time of the article in milliseconds.
    private String mDate;
    // The author of the article.
    private String mAuthor;
    // The website of the article.
    private String mWebsite;

    public Article(String section, String title, String date, String author, String website) {
        mSection = section;
        mTitle = title;
        mDate = date;
        mAuthor = author;
        mWebsite = website;
    }

    // Returns the section name of the article.
    public String getArticleSectionName() {
        return mSection;
    }

    // Returns the title of the article.
    public String getArticleTitle() {
        return mTitle;
    }

    // Returns the time in milliseconds for the article.
    public String getArticleDate() {
        return mDate;
    }

    // Returns the author of the article.
    public String getArticleAuthor() {
        return mAuthor;
    }

    // Returns the website of the article.
    public String getArticleWebsite() {
        return mWebsite;
    }
}
