package com.dicoding.capstones.viewModel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstones.data.model.History
import com.dicoding.capstones.databinding.ItemNewsBinding

class HistoryAdapter(private val onItemClick: ((String) -> Unit)? = null) :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    class HistoryViewHolder(
        private val binding: ItemNewsBinding,
        private val context: Context,
        private val onItemClick: ((String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            Log.d(
                "HistoryAdapter",
                "Binding history: ${history.id}, uri: ${history.uri}, category: ${history.category}, percentage: ${history.percentage}"
            )
            val uri = Uri.parse(history.uri)
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.thumbnailImage.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                Log.e("HistoryAdapter", "Error loading image from URI: ${history.uri}", e)
            }

            binding.news1.text = history.category
            binding.news2.text = history.percentage

            binding.root.setOnClickListener {
                onItemClick?.invoke(history.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, parent.context, onItemClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}
