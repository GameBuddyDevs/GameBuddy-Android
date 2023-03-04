package com.example.gamebuddy.presentation

interface UICommunicationListener {

    fun displayProgressBar(isLoading: Boolean)

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean
}