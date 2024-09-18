package com.aura.domain

import com.google.gson.annotations.SerializedName

data class LoginModel (
    val identifier: String,
    val password: String
)