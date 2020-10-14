package com.example.newsapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "read_later_article")
public class LaterArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String source;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public LaterArticle(String title, String source, String description, String publishedAt, String urlToImage, String url) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

}
