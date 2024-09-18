package com.aura.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    @POST("login")
    suspend fun login(
        @Query(value = "id") username: String,
        @Query(value = "password") password: String
    ): LoginResponse
}

data class LoginCredentials(val username: String, val password: String)
data class LoginResponse(val success: Boolean)