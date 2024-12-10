package com.example.loginsignup

data class NewsResponse(
    val article_list: List<Article>
)

data class Article(
    val published_date: String,
    val source_name: String,
    val source_url: String,
    val title: String,
    val url: String
)
