package com.example.newsapprodrigo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapprodrigo.R
import com.example.newsapprodrigo.adapter.ItemArticleListener
import com.example.newsapprodrigo.adapter.NewsAdapter
import com.example.newsapprodrigo.databinding.FragmentSearchNewsBinding

import com.example.newsapprodrigo.ui.NewsViewModel
import com.example.newsapprodrigo.util.Constans
import com.example.newsapprodrigo.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {
    private val viewModel: NewsViewModel by viewModels()
    private var TAG: String = "Search News Fragment"
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding
    var isLoading = false
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_news,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        var job: Job? = null
        binding.etSearch.addTextChangedListener{ editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        setupObserver()
    }
    private fun setupObserver(){
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { articles->
                        newsAdapter.submitList(articles.toList())
                    }
                }
                is Resource.Error-> {
                    response.message?.let { message->
                        Log.e(TAG,"message occur: $message")
                    }
                }
                is Resource.Loading -> Unit
            }
            toggleProgressBar(response is Resource.Loading)
            isLoading = response is Resource.Loading
        })
        viewModel.navigateToArticleUrl.observe(viewLifecycleOwner, Observer {
                url -> url?.let {
            this.findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                Bundle().apply
                {
                    putSerializable("articleUrl",url)
                })
            viewModel.onItemNavigated()
        }
        })
    }

    private fun toggleProgressBar(show: Boolean) {
        binding.pagSearchProgressBar.isVisible = show
    }

    private val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !viewModel.isLastPage()
            val isAtLastItem = firstVisiblePosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisiblePosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constans.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchNews(binding.etSearch.toString())
                isScrolling = false
            }
        }
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter(ItemArticleListener {
                articleUrl -> viewModel.onItemClicked(articleUrl)
        })
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    }