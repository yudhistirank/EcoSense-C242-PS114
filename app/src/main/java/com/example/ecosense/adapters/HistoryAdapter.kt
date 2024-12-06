package com.example.ecosense.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosense.databinding.ItemHistoryBinding
import com.example.ecosense.models.HistoryItem

class HistoryAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.tvDate.text = historyItem.history.createdAt
            binding.tvResult.text = historyItem.history.result
            binding.tvSuggestion.text = historyItem.history.suggestion
        }
    }
}
