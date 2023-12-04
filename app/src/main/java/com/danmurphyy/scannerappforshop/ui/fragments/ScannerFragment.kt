package com.danmurphyy.scannerappforshop.ui.fragments

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.danmurphyy.scannerappforshop.R
import com.danmurphyy.scannerappforshop.databinding.FragmentScannerBinding
import com.danmurphyy.scannerappforshop.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

@AndroidEntryPoint
class ScannerFragment : Fragment(), ZBarScannerView.ResultHandler {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var scannerView: ZBarScannerView
    private var mediaPlayer: MediaPlayer? = null
    private val args by navArgs<ScannerFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)

        setupScanner()

        return (binding.root)
    }

    private fun setupScanner() {
        scannerView = ZBarScannerView(requireContext())
        binding.root.addView(scannerView)
        scannerView.startCamera()
        scannerView.setResultHandler(this)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.scan_sound)

    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        scannerView.stopCamera()
        mediaPlayer?.start()
        navigate(result.contents)

    }

    private fun navigate(result: String) {
        when (args.fragment) {
            "add" -> {
                val scannedCode =
                    ScannerFragmentDirections.actionScannerFragmentToAddFragment(result)
                findNavController().navigate(scannedCode)
            }
            "search" ->{
                val scannedCode =
                    ScannerFragmentDirections.actionScannerFragmentToSearchFragment(result)
                findNavController().navigate(scannedCode)
            }

            "delete" ->{
                val scannedCode =
                    ScannerFragmentDirections.actionScannerFragmentToDeleteFragment(result)
                findNavController().navigate(scannedCode)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            setupScanner()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            Constants.MY_PERMISSIONS_REQUEST_CAMERA
        )
    }

}