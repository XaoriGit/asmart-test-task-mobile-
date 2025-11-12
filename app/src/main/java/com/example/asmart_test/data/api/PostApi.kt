package com.example.asmart_test.data.api

import com.example.asmart_test.domain.model.post.PostResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PostApi(private val client: HttpClient) {
    suspend fun getPosts(page: Int, pageSize: Int = 25): List<PostResponse> {
        return client.get("posts/") {
            contentType(ContentType.Application.Json)
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }
}