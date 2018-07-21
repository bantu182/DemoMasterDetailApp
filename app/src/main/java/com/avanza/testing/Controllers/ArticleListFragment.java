package com.avanza.testing.Controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avanza.testing.Helpers.Config;
import com.avanza.testing.Helpers.Utils;
import com.avanza.testing.Models.Article;
import com.avanza.testing.R;
import com.avanza.testing.Views.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArticleListFragment extends Fragment {

    private ArrayList<Article> articles;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.article_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchArticles();
            }
        });
        fetchArticles();
        return rootView;
    }



    public void fetchArticles(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Utils.getRequestUrl("all-sections","7"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("OK")){
                                if (response.has("results") && !response.isNull("results")){
                                    JSONArray jsonArticles = response.getJSONArray("results");
                                    articles = new ArrayList<>();
                                    for (int i=0;i<jsonArticles.length();i++){
                                        Article article = new Article();
                                        JSONObject jsonArticle = jsonArticles.getJSONObject(i);
                                        article.setPublishedDate(jsonArticle.getString("published_date"));
                                        article.setSection(jsonArticle.getString("section"));
                                        article.setPublisher(jsonArticle.getString("byline"));
                                        article.setType(jsonArticle.getString("type"));
                                        article.setTitle(jsonArticle.getString("title"));
                                        article.setShortDescription(jsonArticle.getString("abstract"));
                                        article.setUrl(jsonArticle.getString("url"));
                                        article.setThumbnail(jsonArticle.getJSONArray("media").getJSONObject(0).getJSONArray("media-metadata").getJSONObject(1).getString("url"));
                                        articles.add(article);
                                    }

                                    assert recyclerView != null;
                                    setupRecyclerView((RecyclerView) recyclerView);
                                    swipeContainer.setRefreshing(false);
                                    recyclerView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                                }else Utils.showDialog(getContext(),"No Articles Available");
                            }else Utils.showDialog(getContext(),"Invalid Response");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Utils.showDialog(getContext(),error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Config.READ_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecyclerViewAdapter(articles, getContext(), new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article item) {
                ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
                articleDetailFragment.setArticleURL(item.getUrl());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_panel, articleDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }));
    }
}
