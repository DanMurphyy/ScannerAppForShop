package com.danmurphyy.scannerappforshop.domain.useCase

import com.danmurphyy.scannerappforshop.domain.repository.ItemsRepository
import com.danmurphyy.scannerappforshop.model.ModelItems
import javax.inject.Inject

class ItemsUseCase @Inject constructor(
    private val itemsRepository: ItemsRepository,
) {
    fun getUserData(userId: String) = itemsRepository.getUserData(userId)

    fun addItem(userId: String, itemModelItems: ModelItems) =
        itemsRepository.addItem(userId, itemModelItems)

    fun searchItemInfo(userId: String, itemBarCode: String) =
        itemsRepository.searchItemInfo(userId, itemBarCode)
}