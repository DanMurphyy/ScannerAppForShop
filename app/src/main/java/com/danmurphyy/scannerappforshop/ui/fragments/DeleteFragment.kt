package com.danmurphyy.scannerappforshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danmurphyy.scannerappforshop.databinding.FragmentDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFragment : Fragment() {

    private var _binding: FragmentDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDeleteBinding.inflate(inflater, container, false)

        return (binding.root)
    }

}