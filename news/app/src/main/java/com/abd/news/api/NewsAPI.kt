package com.abd.news.api

import com.abd.news.model.ArticleModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {


    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "aa585f52bbf041bc80224b19fe8cdc8e"
    ) : Response<ArticleModel>


    @GET("/v2/everything")
    suspend fun getQueries(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "aa585f52bbf041bc80224b19fe8cdc8e",
    ) : Response<ArticleModel>

    @GET("/v2/everything")
    suspend fun getNewsByDateRange(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String = "aa585f52bbf041bc80224b19fe8cdc8e"
    ): Response<ArticleModel>



}
