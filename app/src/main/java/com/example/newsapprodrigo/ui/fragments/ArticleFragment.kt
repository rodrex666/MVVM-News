package com.example.newsapprodrigo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.example.newsapprodrigo.R
import com.example.newsapprodrigo.databinding.FragmentArticleBinding
import com.example.newsapprodrigo.databinding.ItemArticleBinding
import com.example.newsapprodrigo.models.Article
import com.example.newsapprodrigo.ui.NewsMainActivity
import com.example.newsapprodrigo.ui.NewsViewModel

class ArticleFragment:Fragment(R.layout.fragment_article) {
    private val viewModel:NewsViewModel by viewModels()
    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        val article = args.articleUrl
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.toString())
        }
        return binding.root


    }

}