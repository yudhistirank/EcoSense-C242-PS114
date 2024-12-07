package com.example.ecosense.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosense.R
import com.example.ecosense.adapters.ArticleAdapter
import com.example.ecosense.databinding.FragmentInfoBinding
import com.example.ecosense.models.Article
import com.example.ecosense.models.ArticleResponse
import com.example.ecosense.network.ApiClientArticles
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

        // Setup RecyclerView
        binding.recyclerViewInfo.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArticleAdapter(articleList)
        binding.recyclerViewInfo.adapter = adapter

        // Setup Spinner
        val categories = resources.getStringArray(R.array.category_array)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        // Handle Spinner Selection
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = if (position == 0) "organik" else "anorganik"
                fetchArticlesByCategory(category, adapter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun fetchArticlesByCategory(category: String, adapter: ArticleAdapter) {
        ApiClientArticles.apiService.getArticlesByCategory(category)
            .enqueue(object : Callback<ArticleResponse> {
                override fun onResponse(
                    call: Call<ArticleResponse>,
                    response: Response<ArticleResponse>
                ) {
                    if (response.isSuccessful) {
                        val articles = response.body()?.data?.articles ?: emptyList()
                        adapter.updateData(articles) // Update adapter dengan data baru
                    } else {
                        Log.e("API_ERROR", "Failed to fetch articles: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Error: ${t.message}")
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
