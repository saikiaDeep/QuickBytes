package com.deepsaikia.quickbytes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{
    //4d7512d3754c4a9ab909d5795ed8188f
    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModelArrayList =new ArrayList<>();
        newsRVAdapter= new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }
    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://images.unsplash.com/photo-1532375810709-75b1da00537c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aW5kaWF8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://images.unsplash.com/photo-1614935151651-0bea6508db6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8c2NpZW5jZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://images.unsplash.com/photo-1518770660439-4636190af475?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9naWVzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://images.unsplash.com/photo-1461896836934-ffe607ba8211?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8c3BvcnRzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://images.unsplash.com/photo-1505751172876-fa1923c5c528?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fGhlYWx0aHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://images.unsplash.com/photo-1586899028174-e7098604235b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://images.unsplash.com/photo-1520607162513-77705c0f0d4a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8YnVzaW5lc3N8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVAdapter.notifyDataSetChanged();
    }
    private void getNews(String category) {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=4d7512d3754c4a9ab909d5795ed8188f&category="+category;
        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=4d7512d3754c4a9ab909d5795ed8188f";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitAPI retroFitAPI = retrofit.create(RetroFitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")){
            call=retroFitAPI.getAllNews(url);
        }
        else{
            call=retroFitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel= response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles= newsModel.getArticles();
                for (int i=0;i< articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),
                            articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category=categoryRVModelArrayList.get(position).getCategory();
        getNews(category);

    }
}