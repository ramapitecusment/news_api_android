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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapp.API.ApiClient;
import com.example.newsapp.Adapters.EverythingRecyclerViewAdapter;
import com.example.newsapp.Adapters.TopHeadlinesRecyclerViewAdapter;
import com.example.newsapp.Models.Article;
import com.example.newsapp.Models.LaterArticleViewModel;
import com.example.newsapp.Models.News;
import com.example.newsapp.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EverythingFragment extends Fragment {

    RecyclerView recyclerView;
    EverythingRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText editTextSearch;
    Button buttonSearch;
    NestedScrollView nestedScrollView;
    List<Article> articles = new ArrayList<>();
    List<Article> articles_forCheck = new ArrayList<>();
    List<Article> articles_forAdd = new ArrayList<>();
    private LaterArticleViewModel laterArticleViewModel;
    private int page = 1;
    private int maxPages;
    private int divider;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EverythingFragment() {
    }

    public static EverythingFragment newInstance(String param1, String param2) {
        EverythingFragment fragment = new EverythingFragment();
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
        return inflater.inflate(R.layout.fragment_everything, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerEverything);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        laterArticleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(LaterArticleViewModel.class);

        swipeRefreshLayout = getView().findViewById(R.id.swipeRefresh);
        editTextSearch = getView().findViewById(R.id.editTextSearch);
        buttonSearch = getView().findViewById(R.id.buttonSearch);
        nestedScrollView = getView().findViewById(R.id.nestedScrollView);

        setListeners();
        retrieveJson(editTextSearch.getText().toString());
    }

    // Query
    public void retrieveJson(String query) {
        swipeRefreshLayout.setRefreshing(true);
        Call<News> call;
        if (!query.trim().equals("")) {
            call = ApiClient.getInstance().getApi().getSpecificData(query, Util.API_KEY, page, Util.AMOUNT_OF_NEWS_PER_PAGE);
        } else {
            call = ApiClient.getInstance().getApi().getSpecificData(Util.DEFAULT_QUERY_EVERYTHING, Util.API_KEY, page, Util.AMOUNT_OF_NEWS_PER_PAGE);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    maxPages = Integer.parseInt(response.body().getTotalResults())/Util.AMOUNT_OF_NEWS_PER_PAGE;
                    divider = Integer.parseInt(response.body().getTotalResults())%Util.AMOUNT_OF_NEWS_PER_PAGE;
                    swipeRefreshLayout.setRefreshing(false);
                    articles_forCheck.clear();
                    articles_forCheck = response.body().getArticles();
                    articles_forAdd = Stream.concat(articles.stream(), articles_forCheck.stream())
                            .collect(Collectors.toList());
                    articles.clear();
                    articles = new ArrayList<>(articles_forAdd);
                    adapter = new EverythingRecyclerViewAdapter(getActivity(), articles, laterArticleViewModel);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "onFailure EverythingFragment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(editTextSearch.getText().toString());
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                articles.clear();
                retrieveJson(editTextSearch.getText().toString());
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
                            retrieveJson(editTextSearch.getText().toString());
                        }
                        else if(divider > 0){
                            Log.d("onScrollChange", String.valueOf(page));
                            page++;
                            retrieveJson(editTextSearch.getText().toString());
                        }
                    }
                    Log.d("onScrollChange", "Last page");
                    Log.d("onScrollChange", String.valueOf(articles.size()));
                }
            }
        });
    }
}