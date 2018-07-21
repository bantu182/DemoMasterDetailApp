package com.avanza.testing.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.avanza.testing.R;

public class ArticleDetailFragment extends Fragment {

    private String articleURL;

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);

        WebView articleDetailView = (WebView) rootView.findViewById(R.id.detail_view);
        articleDetailView.loadUrl(getArticleURL());

        return rootView;
    }
}
