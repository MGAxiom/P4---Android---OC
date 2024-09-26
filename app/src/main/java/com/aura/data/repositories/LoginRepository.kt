package com.aura.data.repositories

import com.aura.data.remote.ApiService
import com.aura.data.remote.LoginRequest

class LoginRepository(private val api: ApiService) {
    suspend fun login(id: String, password: String): Boolean {
        return try {
            val response = api.login(LoginRequest(id, password))
            response.granted
        } catch (e: Exception) {
            print(e)
            false
        }
    }
}