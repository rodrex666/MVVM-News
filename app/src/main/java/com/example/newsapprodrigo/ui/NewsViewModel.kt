package com.example.newsapprodrigo.ui

import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapprodrigo.models.Article
import com.example.newsapprodrigo.models.NewsResponse
import com.example.newsapprodrigo.repository.NewsRepository
import com.example.newsapprodrigo.util.Resource
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
):ViewModel() {
    val TAG:String = "NewsViewModel"
    val breakingNews: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
    private val _navigateToArticleUrl = MutableLiveData<String?>()

    val navigateToArticleUrl
    get() = _navigateToArticleUrl
    val newsRepository = NewsRepository()
    init {
        getBreakingNews(NewsMainActivity.Country.getCountry())
        Log.d(TAG,"Enter View Model")
    }
    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode)
        if (response is Resource.Success){
            breakingNews.postValue(response)
        }
    }
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,1)
        if (response is Resource.Success){
            searchNews.postValue(response)
        }
    }

    fun isLastPage(): Boolean{
        return newsRepository.isLastPage()
    }

    fun onItemClicked(url: String){
        _navigateToArticleUrl.value = url
    }
    fun onItemNavigated(){
        _navigateToArticleUrl.value = null
    }
}