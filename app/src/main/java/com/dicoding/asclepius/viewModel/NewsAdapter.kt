package com.dicoding.asclepius.viewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.api.NewsItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class NewsAdapter(private val onItemClick: ((Int) -> Unit)? = null) :
    ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsItem) {
            news.let { it ->
                Glide.with(binding.thumbnail.context)
                    .load(it.urlToImage)
                    .into(binding.thumbnailImage)
                binding.name.text = it.title
                binding.news1.text = it.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(
                oldItem: NewsItem,
                newItem: NewsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NewsItem,
                newItem: NewsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
