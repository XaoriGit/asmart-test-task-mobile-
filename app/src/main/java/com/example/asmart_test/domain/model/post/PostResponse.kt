package com.example.asmart_test.domain.model.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: Int,
    val title: String,
    val body: String,
    @SerialName("img_url") val imgUrl: String
)