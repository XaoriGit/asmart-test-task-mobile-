package com.example.asmart_test.domain.repository

import com.example.asmart_test.domain.model.auth.AuthTokens

interface AuthTokensRepository {
    suspend fun getTokens(): AuthTokens
    suspend fun saveTokens(access: String, refresh: String)
    suspend fun clearTokens()
}