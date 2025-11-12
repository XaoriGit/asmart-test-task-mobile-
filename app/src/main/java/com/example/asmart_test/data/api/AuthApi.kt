package com.example.asmart_test.data.api

import com.example.asmart_test.domain.model.auth.AuthResponse
import com.example.asmart_test.domain.repository.AuthTokensRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApi(
    private val client: HttpClient,
    private val authTokensRepository: AuthTokensRepository
) {
    suspend fun login(username: String, password: String): Boolean {
        return try {
            val response: AuthResponse = client.post("token/pair") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "username" to username,
                        "password" to password
                    )
                )
            }.body()

            authTokensRepository.saveTokens(response.access, response.refresh)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}