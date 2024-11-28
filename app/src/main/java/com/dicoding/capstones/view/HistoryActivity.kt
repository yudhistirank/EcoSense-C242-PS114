package com.dicoding.capstones.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.data.model.History
import com.dicoding.capstones.databinding.ActivityHistoryBinding
import com.dicoding.capstones.viewModel.HistoryAdapter
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "History"

        setupRecyclerView()
        setupAdapter()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.history.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.history.addItemDecoration(itemDecoration)
    }

    private fun setupAdapter() {
        adapter = HistoryAdapter { historyId ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("historyId", historyId)
            }
            Log.d("HistoryActivity", "historyId: $historyId")
            startActivity(intent)
        }
        binding.history.adapter = adapter
    }

    private fun observeViewModel() {
        mainViewModel.history.observe(this) { listItem ->
            setHistory(listItem)
            mainViewModel.clearErrorMessage()
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                mainViewModel.clearErrorMessage()
            }
        }
    }

    private fun setHistory(lifeHistory: List<History?>) {
        adapter.submitList(lifeHistory)
    }
}
