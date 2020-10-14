package com.example.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newsapp.Models.Article;
import com.example.newsapp.Models.LaterArticle;
import com.example.newsapp.Models.LaterArticleViewModel;
import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LaterArticleAdapter extends RecyclerView.Adapter<LaterArticleAdapter.ViewHolder> {
    private Context context;
    private List<LaterArticle> articles = new ArrayList<>();
    private LaterArticleViewModel laterArticleViewModel;

    public LaterArticleAdapter(Context context, LaterArticleViewModel laterArticleViewModel) {
        this.context = context;
        this.laterArticleViewModel = laterArticleViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cards, parent, false);
        return new ViewHolder(view);
    }

    public void setArticles(List<LaterArticle> articles){
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LaterArticle article = articles.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewSource.setText(article.getSource());
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
                intent.putExtra("source", article.getSource());
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
                laterArticleViewModel.delete(articles.get(position));
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
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
