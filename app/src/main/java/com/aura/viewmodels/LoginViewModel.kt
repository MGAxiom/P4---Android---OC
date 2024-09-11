package com.aura.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.data.remote.LoginApiService
import com.aura.data.remote.LoginCredentials
import com.aura.ui.login.LoginActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: LoginApiService): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState


    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginCredentials(username, password))
                if (response.success) {
                    _loginState.value = LoginState.Success(response.success)
                } else {
                    _loginState.value = LoginState.Error("Access not granted")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val success: Boolean) : LoginState()
    data class Error(val message: String) : LoginState()
}