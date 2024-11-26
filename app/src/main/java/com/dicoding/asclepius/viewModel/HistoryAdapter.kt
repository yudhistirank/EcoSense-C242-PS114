package com.dicoding.asclepius.viewModel

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.databinding.ItemNewsBinding

class HistoryAdapter(private val onItemClick: ((String) -> Unit)? = null) :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    class HistoryViewHolder(
        private val binding: ItemNewsBinding,
        private val onItemClick: ((String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            Log.d("HistoryAdapter", "Binding history: ${history.id}, uri: ${history.uri}, category: ${history.category}, percentage: ${history.percentage}")
            binding.thumbnailImage.setImageURI(Uri.parse(history.uri))
            binding.news1.text = history.category
            binding.news2.text = history.percentage

            binding.root.setOnClickListener {
                onItemClick?.invoke(history.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}