package com.dicoding.capstones.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.data.api.NewsItem
import com.dicoding.capstones.databinding.ActivityNewsBinding
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.NewsAdapter
import com.dicoding.capstones.viewModel.ViewModelFactory

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: NewsAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this) as ViewModelProvider.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News"

        initializeRecyclerView()
        initializeAdapter()
        observeViewModelData()
    }

    private fun initializeRecyclerView() {
        binding.news.layoutManager = LinearLayoutManager(this)
        binding.news.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun initializeAdapter() {
        adapter = NewsAdapter()
        binding.news.adapter = adapter
    }

    private fun observeViewModelData() {
        mainViewModel.cancerNews.observe(this) { newsList ->
            newsList?.let {
                updateNews(it)
                mainViewModel.clearErrorMessage()
            }
        }

        mainViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                mainViewModel.clearErrorMessage()
            }
        }
    }

    private fun updateNews(newsItems: List<NewsItem>) {
        val relevantNews = newsItems.filter { it.title != "[Removed]" && it.description != "[Removed]" }
        adapter.submitList(relevantNews)
    }
}
