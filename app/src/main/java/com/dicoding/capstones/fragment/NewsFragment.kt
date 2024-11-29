package com.dicoding.capstones.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.data.api.NewsItem
import com.dicoding.capstones.databinding.FragmentNewsBinding
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.NewsAdapter
import com.dicoding.capstones.viewModel.ViewModelFactory

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[MainViewModel::class.java]

        initializeRecyclerView()
        initializeAdapter()
        observeViewModelData()
    }

    private fun initializeRecyclerView() {
        binding.news.layoutManager = LinearLayoutManager(requireContext())
        binding.news.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun initializeAdapter() {
        adapter = NewsAdapter()
        binding.news.adapter = adapter
    }

    private fun observeViewModelData() {
        mainViewModel.cancerNews.observe(viewLifecycleOwner) { newsList ->
            newsList?.let {
                updateNews(it)
                mainViewModel.clearErrorMessage()
            }
        }

        mainViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                mainViewModel.clearErrorMessage()
            }
        }
    }

    private fun updateNews(newsItems: List<NewsItem>) {
        val relevantNews = newsItems.filter { it.title != "[Removed]" && it.description != "[Removed]" }
        adapter.submitList(relevantNews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
