package com.aura.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.data.repositories.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    private val _balanceState = MutableStateFlow<BalanceState>(BalanceState.Idle)
    val balanceState: StateFlow<BalanceState> = _balanceState


    fun getBalance(id: String) {
        viewModelScope.launch {
            _balanceState.value = BalanceState.Loading
            try {
                val currentBalance = repository.getBalance(id)
                _balanceState.value = if (currentBalance != 0.0) {
                    BalanceState.Success(currentBalance)
                } else {
                    BalanceState.Error("Balance update failed: Could not retrieve current balance for this account.")
                }
            } catch (e: Exception) {
                _balanceState.value = BalanceState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class BalanceState {
    object Idle : BalanceState()
    object Loading : BalanceState()
    data class Success(val balance: Double) : BalanceState()
    data class Error(val message: String) : BalanceState()
}