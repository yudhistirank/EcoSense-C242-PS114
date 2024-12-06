package com.example.ecosense.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosense.adapters.HistoryAdapter
import com.example.ecosense.databinding.FragmentHistoryBinding
import com.example.ecosense.models.HistoryResponse
import com.example.ecosense.network.ApiClient
import com.example.ecosense.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val apiService: ApiService by lazy {
        ApiClient.apiService // Assuming RetrofitClient is set up to provide ApiServicePredict
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data from API
        fetchPredictionHistories()

        return binding.root
    }

    private fun fetchPredictionHistories() {
        // Launch a coroutine in lifecycleScope
        lifecycleScope.launch {
            try {
                // Make the suspend function call within a coroutine
                val response: Response<HistoryResponse> = apiService.getPredictionHistories()

                if (response.isSuccessful && response.body() != null) {
                    val historyList = response.body()!!.data // Access the data field here

                    // Now historyList is of type List<HistoryItem>
                    val adapter = HistoryAdapter(historyList)
                    binding.recyclerViewHistory.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
