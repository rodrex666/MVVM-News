package com.example.newsapprodrigo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapprodrigo.R
import com.example.newsapprodrigo.adapter.ItemArticleListener
import com.example.newsapprodrigo.adapter.NewsAdapter
import com.example.newsapprodrigo.databinding.FragmentBreakNewsBinding
import com.example.newsapprodrigo.ui.NewsMainActivity
import com.example.newsapprodrigo.ui.NewsViewModel
import com.example.newsapprodrigo.util.Constans.Companion.QUERY_PAGE_SIZE
import com.example.newsapprodrigo.util.Resource

class BreakNewsFragment: Fragment(R.layout.fragment_break_news) {
    private val viewModel:NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter
    private lateinit var binding:FragmentBreakNewsBinding
    private var TaG:String = "Break News Fragment"
    var isLoading = false
    var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_break_news,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObserver()
    }

    private fun setupObserver(){
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { articles->
                        newsAdapter.submitList(articles.toList())

                    }
                }
                is Resource.Error-> {
                    response.message?.let { message->
                        Log.e(TaG,"message occur: $message")
                    }
                }
                is Resource.Loading -> Unit
            }
            toggleProgressBar(response is Resource.Loading)
            isLoading = response is Resource.Loading
        })
        viewModel.navigateToArticleUrl.observe(viewLifecycleOwner, Observer {
                url -> url?.let {
            this.findNavController().navigate(R.id.action_breakNewsFragment_to_articleFragment,
                Bundle().apply
                {
                    putSerializable("articleUrl",url)
                })
            viewModel.onItemNavigated()
        }
        })
    }
    private fun toggleProgressBar(show: Boolean) {
        binding.pagProgressBar.isVisible = show
    }

    val scrollListener = object:RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !viewModel.isLastPage()
            val isAtLastItem = firstVisiblePosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisiblePosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews(NewsMainActivity.Country.getCountry())
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
        binding.rvBreakNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakNewsFragment.scrollListener)
        }
    }
}
