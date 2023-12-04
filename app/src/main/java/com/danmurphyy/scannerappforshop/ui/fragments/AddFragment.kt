package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danmurphyy.scannerappforshop.MainActivity
import com.danmurphyy.scannerappforshop.R
import com.danmurphyy.scannerappforshop.databinding.FragmentAddBinding
import com.danmurphyy.scannerappforshop.model.ModelItems
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import com.danmurphyy.scannerappforshop.ui.ItemsViewModel
import com.danmurphyy.scannerappforshop.ui.logs.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment(), IAuthViews {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private var mCode: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        init()
        binding.addItemBtnToDb.setOnClickListener {
            insertItemData()
        }

        binding.buttonScan.setOnClickListener {
            val fragment = AddFragmentDirections.actionAddFragmentToScannerFragment2("add")
            findNavController().navigate(fragment)
        }
        return binding.root
    }

    private fun init() {
        mCode = arguments?.getString("scannedCode") ?: ""
        binding.barcodeView.setText(mCode)
    }

    private fun insertItemData() {
        val mName = binding.editItemName.text.toString()
        val mCategory = binding.editCategory.text.toString()
        val mPrice = binding.editPrice.text.toString()
        mCode = binding.barcodeView.text.toString()
        val validation = verifyDataFromItem(mName, mCategory, mPrice, mCode!!)
        if (validation) {
            val userId = authViewModel.getUserId()
            val itemModel = ModelItems(mName, mCategory, mPrice, mCode!!)
            itemsViewModel.addItem(userId, itemModel, this)
        } else {
            showToast("Please fill out all fields")
        }
    }

    private fun verifyDataFromItem(
        name: String, category: String,
        price: String, code: String,
    ): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(category)
                || TextUtils.isEmpty(price) || TextUtils.isEmpty(code))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        binding.progressBarAdd.visibility = View.VISIBLE
        super.showProgressBar()
    }

    override fun hideProgressBar() {
        binding.progressBarAdd.visibility = View.GONE
        super.hideProgressBar()
    }

    override fun showError(error: String) {
        binding.progressBarAdd.visibility = View.GONE
        showToast(error)
        super.showError(error)
    }

    override fun navigateTo() {
        (requireActivity() as MainActivity).handleBackPress()
        Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
        super.navigateTo()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
