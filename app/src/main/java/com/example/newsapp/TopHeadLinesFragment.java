package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newsapp.API.ApiClient;
import com.example.newsapp.Adapters.TopHeadlinesRecyclerViewAdapter;
import com.example.newsapp.Models.Article;
import com.example.newsapp.Models.LaterArticleViewModel;
import com.example.newsapp.Models.News;
import com.example.newsapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopHeadLinesFragment extends Fragment {

    RecyclerView recyclerView;
    TopHeadlinesRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    List<Article> articles = new ArrayList<>();
    List<Article> firstPage = new ArrayList<>();
    List<Article> articles_forCheck = new ArrayList<>();
    List<Article> articles_forAdd = new ArrayList<>();
    private LaterArticleViewModel laterArticleViewModel;
    private Timer timer;
    private int page = 1;
    private int maxPages;
    private int divider;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TopHeadLinesFragment() {
    }

    public static TopHeadLinesFragment newInstance(String param1, String param2) {
        TopHeadLinesFragment fragment = new TopHeadLinesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_head_lines, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerViewTopHeadlines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = getView().findViewById(R.id.swipeRefresh);
        nestedScrollView = getView().findViewById(R.id.nestedScrollView);
        laterArticleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(LaterArticleViewModel.class);
        setListeners();
        queryFirstPage(getCountry());
        retrieveJson(getCountry());
    }

    public void retrieveJson(final String COUNTRY) {
        swipeRefreshLayout.setRefreshing(true);
        Call<News> call;
        call = ApiClient.getInstance().getApi().getHeadlines(COUNTRY, Util.API_KEY, page, Util.AMOUNT_OF_NEWS_PER_PAGE);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    Log.d("handlerRefresh", "refresh");
                    swipeRefreshLayout.setRefreshing(false);
                    articles_forCheck.clear();
                    articles_forCheck = response.body().getArticles();
                    articles_forAdd = Stream.concat(articles.stream(), articles_forCheck.stream())
                            .collect(Collectors.toList());
//                    articles = response.body().getArticles();
                    articles.clear();
                    articles = new ArrayList<>(articles_forAdd);
                    adapter = new TopHeadlinesRecyclerViewAdapter(getActivity(), articles, laterArticleViewModel);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "onFailure TopHeadLinesFragment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void queryFirstPage(final String COUNTRY) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<News> call;
                call = ApiClient.getInstance().getApi().getHeadlines(COUNTRY, Util.API_KEY, 1, 1);
                call.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                        assert response.body() != null;
                        if (response.isSuccessful() && response.body().getArticles() != null) {
                            firstPage.clear();
                            firstPage = response.body().getArticles();
                            maxPages = Integer.parseInt(response.body().getTotalResults())/Util.AMOUNT_OF_NEWS_PER_PAGE;
                            divider =  Integer.parseInt(response.body().getTotalResults())%Util.AMOUNT_OF_NEWS_PER_PAGE;
                            // TODO
                            if (articles.size() != 0 &&
                                    articles.get(0).getAuthor().equals(firstPage.get(0).getAuthor()) &&
                                    articles.get(0).getPublishedAt().equals(firstPage.get(0).getPublishedAt()) &&
                                    articles.get(0).getDescription().equals(firstPage.get(0).getDescription())) {
                                Log.d("checkForArticleUpdate", "ThereIsNoUpdate");
                            } else {
                                page = 1;
                                articles.clear();
                                retrieveJson(COUNTRY);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                        runUIExecute(false);
                        Toast.makeText(getActivity(), "onFailure queryFirstPage", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5000);
    }

    private void runUIExecute(final boolean refresh) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(refresh);
            }
        });
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry().toLowerCase();
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(getCountry());
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    Log.d("onScrollChange", String.valueOf(scrollY));
                    Log.d("onScrollChange", String.valueOf(v.getChildAt(0).getMeasuredHeight()));
                    Log.d("onScrollChange", String.valueOf(v.getMeasuredHeight()));
                    if (page <= maxPages){
                        if (page < maxPages){
                            Log.d("onScrollChange", String.valueOf(page));
                            page++;
                            retrieveJson(getCountry());
                        }
                        else if(divider > 0){
                            Log.d("onScrollChange", String.valueOf(page));
                            page++;
                            retrieveJson(getCountry());
                        }
                    }
                    Log.d("onScrollChange", "Last page");
                    Log.d("onScrollChange", String.valueOf(articles.size()));
                }
            }
        });
    }
}