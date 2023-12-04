package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danmurphyy.scannerappforshop.databinding.FragmentSearchBinding
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import com.danmurphyy.scannerappforshop.ui.ItemsViewModel
import com.danmurphyy.scannerappforshop.ui.logs.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment(), IAuthViews {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private var mCode: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        init()

        binding.imageButtonSearch.setOnClickListener {
            val fragment = SearchFragmentDirections.actionSearchFragmentToScannerFragment("search")
            findNavController().navigate(fragment)
        }

        binding.searchBtn.setOnClickListener {
            mCode = binding.searchField.text.toString()
            showItemDetails(mCode!!)
        }

        return (binding.root)
    }

    private fun showItemDetails(itemCode: String) {
        Log.d("SearchFragmentNow", "showItemDetails $itemCode")
        val userId = authViewModel.getUserId()
        itemsViewModel.searchItemInfo(userId, itemCode, this)
        CoroutineScope(Dispatchers.IO).launch {
            itemsViewModel.itemData.collectLatest { item ->
                withContext(Dispatchers.Main) {
                    binding.layoutSearch.viewitemname.text = item?.itemName.toString()
                    binding.layoutSearch.viewitembarcode.text = item?.itemBarCode.toString()
                    binding.layoutSearch.viewitemcategory.text = item?.itemCategory.toString()
                    binding.layoutSearch.viewitemprice.text = item?.itemPrice.toString()
                }
            }
        }
    }

    private fun init() {
        mCode = arguments?.getString("scannedCode") ?: ""
        if (!mCode.isNullOrEmpty()) {
            binding.searchField.setText(mCode)
            showItemDetails(mCode!!)
        }
    }

    override fun showProgressBar() {
        binding.progressBarSearch.visibility = View.VISIBLE
        binding.layoutSearch.root.visibility = View.INVISIBLE
        super.showProgressBar()
    }

    override fun hideProgressBar() {
        binding.progressBarSearch.visibility = View.INVISIBLE
        binding.layoutSearch.root.visibility = View.VISIBLE
        super.hideProgressBar()
    }

    override fun showError(error: String) {
        binding.progressBarSearch.visibility = View.INVISIBLE
        showToast(error)
        super.showError(error)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}