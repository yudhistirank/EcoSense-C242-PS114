package com.example.ecosense.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosense.adapters.ArticleAdapter
import com.example.ecosense.databinding.FragmentInfoBinding
import com.example.ecosense.models.Article
import com.example.ecosense.models.ArticleResponse
import com.example.ecosense.network.ApiClientArticles  // Pastikan ini yang digunakan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private val articleList = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewInfo.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArticleAdapter(articleList)
        binding.recyclerViewInfo.adapter = adapter

        fetchArticles(adapter)
    }

    private fun fetchArticles(adapter: ArticleAdapter) {
        ApiClientArticles.apiService.getArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.articles?.let {
                        articleList.clear()
                        articleList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Log.e("InfoFragment", "Error fetching articles: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
