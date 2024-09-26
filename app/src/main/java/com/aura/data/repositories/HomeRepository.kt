package com.aura.data.repositories

import com.aura.data.remote.ApiService

class HomeRepository(private val api: ApiService) {
    suspend fun getBalance(id: String): Double {
        return try {
            val accounts = api.getAccount(id)
            val mainAccount = accounts.find { it.isMain }
            mainAccount?.balance ?: 0.0
        } catch (e: Exception) {
            print(e)
            0.0
        }
    }
}