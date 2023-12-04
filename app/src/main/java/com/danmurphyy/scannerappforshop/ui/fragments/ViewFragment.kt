package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danmurphyy.scannerappforshop.databinding.FragmentViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewFragment : Fragment() {

    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentViewBinding.inflate(inflater, container, false)

        return (binding.root)
    }

}