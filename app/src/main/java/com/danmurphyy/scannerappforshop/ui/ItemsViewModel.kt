package com.danmurphyy.scannerappforshop.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.danmurphyy.scannerappforshop.domain.useCase.ItemsUseCase
import com.danmurphyy.scannerappforshop.model.ModelItems
import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsUseCase: ItemsUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private lateinit var iViews: IAuthViews

    private val _userData: MutableStateFlow<ModelUser?> = MutableStateFlow(ModelUser())
    val userData: StateFlow<ModelUser?> = _userData
    private val _itemData: MutableStateFlow<ModelItems?> = MutableStateFlow(ModelItems())
    val itemData: StateFlow<ModelItems?> = _itemData
    private var _allItems = MutableStateFlow<List<ModelItems>>(emptyList())
    var allItems: StateFlow<List<ModelItems>> = _allItems

    fun getUserData(userId: String, listener: IAuthViews) {
        iViews = listener
        viewModelScope.launch {
            itemsUseCase.getUserData(userId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        _userData.value = it.data
                    }
                }
            }
        }
    }

    fun addItem(userId: String, itemModelItems: ModelItems, listener: IAuthViews) {
        iViews = listener
        viewModelScope.launch {
            itemsUseCase.addItem(userId, itemModelItems).collectLatest {
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

    fun searchItemInfo(userId: String, itemBarCode: String, listener: IAuthViews) {
        iViews = listener
        viewModelScope.launch {
            itemsUseCase.searchItemInfo(userId, itemBarCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        _itemData.value = it.data
                    }
                }
            }
        }
    }

    fun getAllItems(userId: String, listener: IAuthViews) {
        iViews = listener
        viewModelScope.launch {
            itemsUseCase.getAllItems(userId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        _allItems.value = it.data
                    }
                }
            }
        }
    }

    fun deleteItem(userId: String, itemCode: String, listener: IAuthViews) {
        iViews = listener
        viewModelScope.launch {
            itemsUseCase.deleteItem(userId, itemCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }

                    is Resource.Error -> {
                        iViews.showError(it.message)
                    }

                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.showError("Deleted Successfully!")
                    }
                }
            }
        }
    }
}