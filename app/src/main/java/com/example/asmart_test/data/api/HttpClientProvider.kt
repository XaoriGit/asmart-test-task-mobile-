package com.example.asmart_test.data.api

import com.example.asmart_test.domain.model.auth.AuthResponse
import com.example.asmart_test.domain.repository.AuthTokensRepository
import com.example.asmart_test.domain.repository.LogoutHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

fun createHttpClient(
    authTokensRepository: AuthTokensRepository,
): HttpClient {
    return HttpClient(CIO) {
        defaultRequest { url("http://89.208.106.238/api/") }
        expectSuccess = true
        install(Auth) {
            bearer {
                loadTokens {
                    val tokens = runBlocking { authTokensRepository.getTokens() }
                    if (tokens.accessToken != null) {
                        BearerTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken ?: ""
                        )
                    } else null
                }

                refreshTokens {
                    val oldTokens = oldTokens ?: return@refreshTokens null

                    try {
                        val response: AuthResponse = client.post("token/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(mapOf("refresh" to oldTokens.refreshToken))
                        }.body()

                        runBlocking {
                            authTokensRepository.saveTokens(response.access, response.refresh)
                        }

                        BearerTokens(response.access, response.refresh)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runBlocking { authTokensRepository.clearTokens() }
                        null
                    }
                }

                sendWithoutRequest { request ->
                    !request.url.encodedPath.contains("token/")
                }
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 8_000
            connectTimeoutMillis = 5_000
            socketTimeoutMillis = 8_000
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
}
