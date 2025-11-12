package com.example.asmart_test.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val access: String,
    val refresh: String
)