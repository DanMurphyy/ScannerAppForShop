package com.danmurphyy.scannerappforshop.ui

interface IAuthViews {
    fun showProgressBar() {}

    fun hideProgressBar() {}

    fun showError(error: String) {}

    fun navigateTo() {}
}