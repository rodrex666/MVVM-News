package com.example.newsapprodrigo.models

import com.example.newsapprodrigo.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)