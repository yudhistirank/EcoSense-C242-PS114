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
import com.dicoding.capstones.data.model.History
import com.dicoding.capstones.databinding.FragmentHistoryBinding
import com.dicoding.capstones.viewModel.HistoryAdapter
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[MainViewModel::class.java]

        setupRecyclerView()
        setupAdapter()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.history.layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.history.addItemDecoration(itemDecoration)
    }

    private fun setupAdapter() {
        adapter = HistoryAdapter { historyId ->
            Toast.makeText(requireContext(), "Clicked $historyId", Toast.LENGTH_SHORT).show()
        }
        binding.history.adapter = adapter
    }

    private fun observeViewModel() {
        mainViewModel.history.observe(viewLifecycleOwner) { listItem ->
            setHistory(listItem)
        }

        mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHistory(lifeHistory: List<History?>) {
        adapter.submitList(lifeHistory)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
