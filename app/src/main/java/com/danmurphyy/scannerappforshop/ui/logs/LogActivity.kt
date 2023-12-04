package com.danmurphyy.scannerappforshop.ui.logs

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.danmurphyy.scannerappforshop.MainActivity
import com.danmurphyy.scannerappforshop.databinding.ActivityLogBinding
import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogActivity : AppCompatActivity(), IAuthViews {
    private lateinit var binding: ActivityLogBinding
    private val authViewModel: AuthenticationViewModel by viewModels()

    companion object {
        private const val SIGN_IN_VIEW = "SIGN_IN_VIEW"
        private const val SIGN_UP_VIEW = "SIGN_UP_VIEW"
    }

    private var currentVisibleView: String = SIGN_IN_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeUnitsVisible()
        if (checkAuthenticationStatus()) {
            navigateTo()
        }

        binding.btnSignIn.setOnClickListener {
            insertSignIn()
        }

        binding.btnRegister.setOnClickListener {
            insertRegister()
        }

        binding.forgotPassword.setOnClickListener {
            resetPassword()
        }

    }

    private fun makeUnitsVisible() {
        binding.rgSignings.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbSignIn.id -> {
                    makeVisibleSignInView()
                }

                binding.rbSignUp.id -> {
                    makeVisibleRegisterView()
                }
            }
        }
    }

    private fun makeVisibleSignInView(): Boolean {
        currentVisibleView = SIGN_IN_VIEW
        binding.loLogin.visibility = View.VISIBLE
        binding.loRegister.visibility = View.GONE

        binding.emailRegister.text?.clear()
        binding.passwordRegister.text?.clear()
        binding.confirmPasswordRegister.text?.clear()
        binding.departmentNameRegister.text?.clear()

        return true
    }

    private fun makeVisibleRegisterView(): Boolean {
        currentVisibleView = SIGN_UP_VIEW
        binding.loLogin.visibility = View.GONE
        binding.loRegister.visibility = View.VISIBLE

        binding.emailSignIn.text?.clear()
        binding.passwordSignIn.text?.clear()

        return true
    }

    override fun showProgressBar() {
        super.showProgressBar()
        binding.progressBar.visibility = View.VISIBLE
        binding.loSign.visibility = View.GONE
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        binding.progressBar.visibility = View.GONE
        binding.loSign.visibility = View.VISIBLE
    }

    override fun showError(error: String) {
        super.showError(error)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = View.GONE
        binding.loSign.visibility = View.VISIBLE
    }

    override fun navigateTo() {
        Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun insertSignIn() {
        val mEmail = binding.emailSignIn.text.toString()
        val mPassword = binding.passwordSignIn.text.toString()
        val validation = verifyDataFromUser(mEmail, mPassword)

        if (validation) {
            authViewModel.emailSignIn(mEmail, mPassword, this)
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun insertRegister() {
        val rEmail = binding.emailRegister.text.toString()
        val rPassword = binding.passwordRegister.text.toString()
        val rConfirmPassword = binding.confirmPasswordRegister.text.toString()
        val depName = binding.departmentNameRegister.text.toString()
        val rValidation = verifyDataFromUserReg(rEmail, rPassword, depName)
        if (rValidation) {
            if (rPassword == rConfirmPassword) {
                val modelUser = ModelUser(rEmail, depName)
                authViewModel.createUserProfile(rEmail, rPassword, modelUser, this)
            } else {
                Toast.makeText(this, "Please make sure passwords are same", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun verifyDataFromUser(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
    }

    private fun verifyDataFromUserReg(email: String, password: String, depName: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
            depName
        ))
    }

    private fun checkAuthenticationStatus(): Boolean {
        return authViewModel.isUserAuthenticated()
    }

    private fun resetPassword() {
        val resetEmail = binding.emailSignIn.text.toString()
        if (resetEmail.isEmpty()) {
            binding.emailSignIn.error = "It's empty"
            binding.emailSignIn.requestFocus()
            return
        } else {
            authViewModel.resetPassword(resetEmail, this)
        }
    }

}