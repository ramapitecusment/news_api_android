package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;

import com.example.newsapp.Adapters.LaterArticleAdapter;
import com.example.newsapp.Models.LaterArticle;
import com.example.newsapp.Models.LaterArticleViewModel;

import java.util.List;
import java.util.Objects;


public class ReadLaterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LaterArticleViewModel laterArticleViewModel;
    RecyclerView recyclerView;
    Button buttonDelete;

    private String mParam1;
    private String mParam2;

    public ReadLaterFragment() {
    }

    public static ReadLaterFragment newInstance(String param1, String param2) {
        ReadLaterFragment fragment = new ReadLaterFragment();
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
        return inflater.inflate(R.layout.fragment_read_later, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonDelete = getView().findViewById(R.id.buttonDelete);

        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerViewReadLater);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        laterArticleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(LaterArticleViewModel.class);

        LaterArticleAdapter adapter = new LaterArticleAdapter(getActivity(), laterArticleViewModel);
        recyclerView.setAdapter(adapter);

        laterArticleViewModel.getAllArticles().observe(getActivity(), new Observer<List<LaterArticle>>() {
            @Override
            public void onChanged(List<LaterArticle> laterArticles) {
                //update RecyclerView
                adapter.setArticles(laterArticles);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laterArticleViewModel.deleteAllArticles();
            }
        });

    }
}