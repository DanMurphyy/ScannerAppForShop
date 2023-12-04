package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danmurphyy.scannerappforshop.databinding.FragmentViewBinding
import com.danmurphyy.scannerappforshop.model.ModelItems
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import com.danmurphyy.scannerappforshop.ui.ItemsAdapter
import com.danmurphyy.scannerappforshop.ui.ItemsViewModel
import com.danmurphyy.scannerappforshop.ui.logs.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ViewFragment : Fragment(), IAuthViews {

    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!
    private val itemsAdapter: ItemsAdapter by lazy { ItemsAdapter() }
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        showRecyclerView()
        return (binding.root)
    }

    private fun showRecyclerView() {
        val itemsRV = binding.recyclerViewsView
        itemsRV.adapter = itemsAdapter
        itemsRV.layoutManager = LinearLayoutManager(requireContext())
        itemsViewModel.getAllItems(authViewModel.getUserId(), this)
        CoroutineScope(Dispatchers.IO).launch {
            itemsViewModel.allItems.collectLatest { item ->
                withContext(Dispatchers.Main) {
                    itemsAdapter.differ.submitList(item)
                    binding.totalNoItem.text = item.size.toString()
                    val totalSum = calculateTotalSum(item)
                    binding.totalSum.text = totalSum.toString()
                }
            }
        }
    }

    private fun calculateTotalSum(items: List<ModelItems>): Double {
        var sum = 0.0
        for (item in items) {
            sum += item.itemPrice.toDoubleOrNull() ?: 0.0
        }
        return sum
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        binding.progressBarView.visibility = View.VISIBLE
        binding.recyclerViewsView.visibility = View.INVISIBLE
        super.showProgressBar()
    }

    override fun hideProgressBar() {
        binding.progressBarView.visibility = View.GONE
        binding.recyclerViewsView.visibility = View.VISIBLE
        super.hideProgressBar()
    }

    override fun showError(error: String) {
        binding.progressBarView.visibility = View.GONE
        showToast(error)
        super.showError(error)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}