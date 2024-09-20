package com.aura.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

data class LoginRequest(val id: String, val password: String)
data class LoginResponse(val granted: Boolean)