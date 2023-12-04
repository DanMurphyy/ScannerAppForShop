package com.danmurphyy.scannerappforshop.domain.useCase

import com.danmurphyy.scannerappforshop.domain.repository.AuthRepository
import com.danmurphyy.scannerappforshop.model.ModelUser
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun emailSignIn(email: String, password: String) = authRepository.emailSignIn(email, password)

    fun isUserAuthenticated() = authRepository.isUserAuthenticated()

    fun getUserId() = authRepository.getUserId()

    fun createUserProfile(email: String, password: String, modelUser: ModelUser) =
        authRepository.createUserProfile(email, password, modelUser)

    fun resetPassword(email: String) = authRepository.resetPassword(email)
}