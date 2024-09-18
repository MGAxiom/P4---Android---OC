package com.aura.data

import com.aura.domain.LoginModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "id")
    val identifier: String,
    @Json(name = "password")
    val password: String
) {
    fun toLoginModel(): LoginModel {
        return LoginModel(
            identifier = identifier,
            password = password
        )
    }
}