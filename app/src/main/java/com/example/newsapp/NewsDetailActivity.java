package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView imageNews;
    TextView textViewTitle, textViewSource, textViewDate, textViewDescription;
    WebView webView;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        imageNews = findViewById(R.id.imageNews);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSource = findViewById(R.id.textViewSource);
        textViewDate = findViewById(R.id.textViewDate);
        textViewDescription = findViewById(R.id.textViewDescription);
        webView = findViewById(R.id.webView);
        loader = findViewById(R.id.webViewLoader);
        loader.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        String desc = intent.getStringExtra("desc");
        String imageUrl = intent.getStringExtra("imageUrl");
        String url = intent.getStringExtra("url");

        textViewTitle.setText(title);
        textViewSource.setText(source);
        textViewDate.setText(time);
        textViewDescription.setText(desc);

        Picasso.get().load(imageUrl).into(imageNews);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        if (webView.isShown()){
            loader.setVisibility(View.INVISIBLE);
        }
    }
}