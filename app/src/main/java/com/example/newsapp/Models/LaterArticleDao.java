package com.example.newsapp.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LaterArticleDao {

    @Insert
    void insert(LaterArticle laterArticle);

    @Update
    void update(LaterArticle laterArticle);

    @Delete
    void delete(LaterArticle laterArticle);

    @Query("DELETE FROM read_later_article")
    void deleteAllArticles();

    @Query("SELECT * FROM read_later_article")
    LiveData<List<LaterArticle>> getAllArticles();

}
