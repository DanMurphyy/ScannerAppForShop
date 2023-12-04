package com.danmurphyy.scannerappforshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danmurphyy.scannerappforshop.databinding.ActivityLogBinding
import com.danmurphyy.scannerappforshop.databinding.ActivityMainBinding
import com.danmurphyy.scannerappforshop.ui.logs.LogActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun handleBackPress() {
        onBackPressedDispatcher.onBackPressed()
    }
}