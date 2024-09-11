package com.aura.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse
}

data class LoginCredentials(val username: String, val password: String)
data class LoginResponse(val success: Boolean)