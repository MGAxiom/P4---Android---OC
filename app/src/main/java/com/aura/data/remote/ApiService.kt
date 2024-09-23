package com.aura.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "id") val id: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "granted") val granted: Boolean
)