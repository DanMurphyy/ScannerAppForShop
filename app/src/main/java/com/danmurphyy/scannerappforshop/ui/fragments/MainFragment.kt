package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danmurphyy.scannerappforshop.R
import com.danmurphyy.scannerappforshop.databinding.FragmentMainBinding
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
class MainFragment : Fragment(), IAuthViews {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.addItems.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }

        binding.searchItems.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        showUserName()

        return (binding.root)
    }

    private fun showUserName(){
        val userId = authViewModel.getUserId()
        itemsViewModel.getUserData(userId, this)
        CoroutineScope(Dispatchers.IO).launch {
            itemsViewModel.userData.collectLatest { user ->
                withContext(Dispatchers.Main){
                    val existingText = binding.firebaseName.text.toString()
                    val newText = "$existingText ${user?.depName}"
                    binding.firebaseName.text = newText
                }
            }
        }
    }

    override fun showProgressBar() {
        binding.progressBarMain.visibility = View.VISIBLE
        binding.loMain.visibility = View.GONE
        super.showProgressBar()

    }

    override fun hideProgressBar() {
        binding.progressBarMain.visibility = View.GONE
        binding.loMain.visibility = View.VISIBLE
        super.hideProgressBar()
    }

    override fun showError(error: String) {
        binding.loMain.visibility = View.GONE
        binding.progressBarMain.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        super.showError(error)
    }
}