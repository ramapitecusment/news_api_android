package com.example.newsapp.API;

import com.example.newsapp.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// HERE YOU MAKE QUERIES

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );

    @GET("everything")
    Call<News> getSpecificData(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("page") Integer page,
            @Query("pageSize") Integer pageSize
    );
}
