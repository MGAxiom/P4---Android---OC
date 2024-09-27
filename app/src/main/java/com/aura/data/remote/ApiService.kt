package com.aura.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: String): List<AccountResponse>

    @POST("transfer")
    suspend fun transfer(@Body transferRequest: TransferRequest): TransferResponse
}

@JsonClass(generateAdapter = true)
data class TransferRequest(
    @Json(name = "sender") val sender: String,
    @Json(name = "recipient") val recipient: String,
    @Json(name = "amount") val amount: Double
)

@JsonClass(generateAdapter = true)
data class TransferResponse(
    @Json(name = "result") val result: Boolean
)

@JsonClass(generateAdapter = true)
data class AccountResponse(
    @Json(name = "id") val id: String,
    @Json(name = "main") val isMain: Boolean,
    @Json(name = "balance") val balance: Double
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "id") val id: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "granted") val granted: Boolean
)