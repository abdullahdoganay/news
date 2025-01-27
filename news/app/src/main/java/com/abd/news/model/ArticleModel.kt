package com.abd.news.model

data class ArticleModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)