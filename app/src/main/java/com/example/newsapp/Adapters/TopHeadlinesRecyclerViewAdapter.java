package com.example.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newsapp.Models.Article;
import com.example.newsapp.Models.LaterArticle;
import com.example.newsapp.Models.LaterArticleViewModel;
import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.format.SimpleTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TopHeadlinesRecyclerViewAdapter extends RecyclerView.Adapter<TopHeadlinesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Article> articles;
    private LaterArticleViewModel laterArticleViewModel;

    public TopHeadlinesRecyclerViewAdapter(Context context, List<Article> articles, LaterArticleViewModel laterArticleViewModel) {
        this.context = context;
        this.articles = articles;
        this.laterArticleViewModel = laterArticleViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = articles.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewSource.setText(article.getSource().getName());
        holder.textViewDate.setText(dateTime(article.getPublishedAt()));

        String imageUrl = article.getUrlToImage();

        Glide.with(context)
                .load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageNews);
//        Picasso.get().load(imageUrl).into(holder.imageNews);

        holder.cardViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("title", article.getTitle());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("time", dateTime(article.getPublishedAt()));
                intent.putExtra("desc", article.getDescription());
                intent.putExtra("imageUrl", article.getUrlToImage());
                intent.putExtra("url", article.getUrl());
                context.startActivity(intent);
            }
        });

        holder.imageViewFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = article.getTitle();
                String source = article.getSource().getName();
                String description = article.getDescription();
                String publishedAt = article.getPublishedAt();
                String urlToImage = article.getUrlToImage();
                String url = article.getUrl();

                LaterArticle laterArticle = new LaterArticle(title, source, description, publishedAt, urlToImage, url);
                laterArticleViewModel.insert(laterArticle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNews, imageViewFavourite;
        TextView textViewTitle, textViewSource, textViewDate;
        CardView cardViewNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNews = itemView.findViewById(R.id.imageNews);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSource = itemView.findViewById(R.id.textViewSource);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            cardViewNews = itemView.findViewById(R.id.cardViewNews);
            imageViewFavourite = itemView.findViewById(R.id.imageViewFavourite);
        }
    }

    public String dateTime(String t) {
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry().toLowerCase();
    }
}
