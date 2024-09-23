package com.aura.data

import com.aura.data.remote.LoginApiService
import com.aura.data.remote.LoginRequest

class LoginRepository(private val api: LoginApiService) {
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