package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danmurphyy.scannerappforshop.databinding.FragmentDeleteBinding
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import com.danmurphyy.scannerappforshop.ui.ItemsViewModel
import com.danmurphyy.scannerappforshop.ui.logs.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteFragment : Fragment(), IAuthViews {

    private var _binding: FragmentDeleteBinding? = null
    private val binding get() = _binding!!
    private var mCode: String? = null
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDeleteBinding.inflate(inflater, container, false)
        init()
        binding.btnScanDelete.setOnClickListener {
            val fragment = DeleteFragmentDirections.actionDeleteFragmentToScannerFragment("delete")
            findNavController().navigate(fragment)
        }

        binding.btnDeleteItem.setOnClickListener {
            mCode = binding.etBarcodeDelete.text.toString()
            deleteItem(mCode!!)
        }

        return (binding.root)
    }

    private fun deleteItem(dCode: String) {
        val userId = authViewModel.getUserId()
        if (!dCode.isEmpty()) {
            itemsViewModel.deleteItem(userId, dCode, this)
            binding.etBarcodeDelete.text.clear()
        } else {
            showToast("Please insert code")
        }
    }

    private fun init() {
        mCode = arguments?.getString("scannedCode") ?: ""
        if (!mCode.isNullOrEmpty()) {
            binding.etBarcodeDelete.setText(mCode)
        }
    }

    override fun showProgressBar() {
        binding.progressBarDelete.visibility = View.VISIBLE
        super.showProgressBar()
    }

    override fun hideProgressBar() {
        binding.progressBarDelete.visibility = View.INVISIBLE
        super.hideProgressBar()
    }

    override fun showError(error: String) {
        binding.progressBarDelete.visibility = View.INVISIBLE
        showToast(error)
        super.showError(error)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}