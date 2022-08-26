package com.example.newsapprodrigo.api

import com.example.newsapprodrigo.models.NewsResponse
import com.example.newsapprodrigo.ui.NewsMainActivity
import com.example.newsapprodrigo.util.Constans.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
@GET("v2/top-headlines")
suspend fun getBreakingNews(
    @Query("country")
    countryCode: String = NewsMainActivity.Country.getCountry(),
    @Query("page")
    pageNumber: Int = 1,
    @Query("apiKey")
    apiKey: String = API_KEY

):Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY

    ):Response<NewsResponse>
}