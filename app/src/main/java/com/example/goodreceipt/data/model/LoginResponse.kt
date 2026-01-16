package com.example.goodreceipt.data.model

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val message: String
)
