package com.example.asmart_test.core

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()
    object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}