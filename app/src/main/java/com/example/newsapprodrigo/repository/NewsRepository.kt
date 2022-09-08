package com.example.newsapprodrigo.repository

import com.example.newsapprodrigo.api.RetrofitInstance
import com.example.newsapprodrigo.models.Article
import com.example.newsapprodrigo.models.NewsResponse
import com.example.newsapprodrigo.util.Constans
import com.example.newsapprodrigo.util.Resource

class NewsRepository {
    var breakingNewsPage = 1
    //val allArticles = mutableListOf<Article>()
    var totalArticles: Int = 0
    suspend fun getBreakingNews(country: String): Resource<List<Article>> {
        val allArticles = mutableListOf<Article>()
        val response = RetrofitInstance.api.getBreakingNews(country, breakingNewsPage)
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                allArticles.addAll(resultResponse.articles)
                totalArticles = resultResponse.totalResults
                return Resource.Success(allArticles)
            }
        }
        return Resource.Error(response.message())
    }
    suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<List<Article>>{
        val allArticles = mutableListOf<Article>()
        val response = RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                allArticles.addAll(resultResponse.articles)
                totalArticles = resultResponse.totalResults
                return Resource.Success(allArticles)
            }
        }
        return Resource.Error(response.message())
    }
    fun isLastPage(): Boolean {
        val totalPages = totalArticles / Constans.QUERY_PAGE_SIZE + 2
        return breakingNewsPage == totalPages
    }

}