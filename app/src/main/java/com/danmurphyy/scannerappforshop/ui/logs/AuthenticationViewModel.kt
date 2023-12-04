package com.danmurphyy.scannerappforshop.ui.logs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.danmurphyy.scannerappforshop.domain.useCase.AuthenticationUseCase
import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.ui.IAuthViews
import com.danmurphyy.scannerappforshop.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private lateinit var iViews: IAuthViews

    fun emailSignIn(email: String, password: String, activity: LogActivity) {
        iViews = activity
        viewModelScope.launch {
            authUseCase.emailSignIn(email, password).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.navigateTo()
                    }
                }
            }
        }
    }

    fun createUserProfile(
        email: String,
        password: String,
        modelUser: ModelUser,
        activity: LogActivity,
    ) {
        iViews = activity
        viewModelScope.launch {
            authUseCase.createUserProfile(email, password, modelUser).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.navigateTo()
                    }
                }
            }
        }
    }

    fun resetPassword(email: String, activity: LogActivity) {
        iViews = activity
        viewModelScope.launch {
            authUseCase.resetPassword(email).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.showError("We have sent you instructions to reset your password!")
                    }
                }
            }
        }
    }

    fun isUserAuthenticated() = authUseCase.isUserAuthenticated()

    fun getUserId() = authUseCase.getUserId()
}