package com.example.ecosense.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosense.adapters.HistoryAdapter
import com.example.ecosense.databinding.FragmentHistoryBinding
import com.example.ecosense.models.PredictionHistory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyList = mutableListOf<PredictionHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HistoryAdapter(historyList)
        binding.recyclerViewHistory.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        // TODO: Load history from API
        historyList.add(PredictionHistory("Plastic Bottle", "Anorganic", "2024-12-03"))
        historyList.add(PredictionHistory("Banana Peel", "Organic", "2024-12-02"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
