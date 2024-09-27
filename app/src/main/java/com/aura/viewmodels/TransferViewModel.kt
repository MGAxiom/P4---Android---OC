package com.aura.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.data.repositories.LoginRepository
import com.aura.data.repositories.TransferRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransferViewModel(private val repository: TransferRepository): ViewModel() {
    private val _transferState = MutableStateFlow<TransferState>(TransferState.Idle)
    val transferState: StateFlow<TransferState> = _transferState


    fun transferFunds(sender: String, recipient: String, amount: Double) {
        viewModelScope.launch {
            _transferState.value = TransferState.Loading
            try {
                val isTransferSuccessful = repository.transferFunds(sender, recipient, amount)
                _transferState.value = if (isTransferSuccessful) {
                    TransferState.Success(isTransferSuccessful)
                } else {
                    TransferState.Error("Transfer failed: could not transfer funds")
                }
            } catch (e: Exception) {
                _transferState.value = TransferState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class TransferState {
    object Idle : TransferState()
    object Loading : TransferState()
    data class Success(val result: Boolean) : TransferState()
    data class Error(val message: String) : TransferState()
}