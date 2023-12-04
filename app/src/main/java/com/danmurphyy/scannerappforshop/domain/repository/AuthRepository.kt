package com.danmurphyy.scannerappforshop.domain.repository

import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun emailSignIn(email: String,password: String): Flow<Resource<Boolean>>
    fun isUserAuthenticated(): Boolean
    fun getUserId(): String
    fun createUserProfile(email: String,password: String, modelUser: ModelUser): Flow<Resource<Boolean>>
    fun resetPassword(email: String): Flow<Resource<Boolean>>

}