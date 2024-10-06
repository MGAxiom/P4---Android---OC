package com.aura.data.repositories

import com.aura.data.remote.ApiService
import com.aura.data.remote.LoginRequest
import com.aura.data.remote.TransferRequest
import kotlinx.coroutines.delay

class TransferRepository(private val api: ApiService) {
    suspend fun transferFunds(sender: String, recipient: String, amount: Double): Boolean {
        delay(1000)
        val transfer = api.transfer(TransferRequest(sender, recipient, amount))
        return transfer.result
    }
}

