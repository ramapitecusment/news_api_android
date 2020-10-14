package com.example.newsapp.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LaterArticleViewModel extends AndroidViewModel {
    private LaterArticleRepository repository;
    private LiveData<List<LaterArticle>> allArticles;

    public LaterArticleViewModel(@NonNull Application application) {
        super(application);

        repository = new LaterArticleRepository(application);
        allArticles = repository.getAllArticle();
    }

    public void insert(LaterArticle laterArticle){
        repository.insert(laterArticle);
    }

    public void update(LaterArticle laterArticle){
        repository.update(laterArticle);
    }

    public void delete(LaterArticle laterArticle){
        repository.delete(laterArticle);
    }

    public void deleteAllArticles(){
        repository.deleteAllArticles();
    }

    public LiveData<List<LaterArticle>> getAllArticles() {
        return allArticles;
    }
}
