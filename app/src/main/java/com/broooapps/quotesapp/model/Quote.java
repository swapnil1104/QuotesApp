package com.broooapps.quotesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class Quote {

    @SerializedName("text")
    private String text;

    @SerializedName("url")
    private String url;

    @SerializedName("author")
    private String author;

    public Quote(String text, String author, String url) {
        this.text = text;
        this.author = author;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
