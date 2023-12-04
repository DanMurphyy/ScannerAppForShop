package com.danmurphyy.scannerappforshop.domain.repository

import com.danmurphyy.scannerappforshop.model.ModelItems
import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    fun getUserData(userId: String): Flow<Resource<ModelUser>>
    fun addItem(userId: String, itemModelItems: ModelItems): Flow<Resource<Boolean>>
    fun searchItemInfo(userId: String, itemBarCode: String): Flow<Resource<ModelItems>>
}