package com.aura.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aura.data.remote.LoginApiService

class LoginViewModelFactory(private val apiService: LoginApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginViewModel(apiService) as T
}