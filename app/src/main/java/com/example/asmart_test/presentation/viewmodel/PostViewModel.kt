package com.example.asmart_test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asmart_test.core.UiState
import com.example.asmart_test.data.api.PostApi
import com.example.asmart_test.domain.model.post.PostResponse
import com.example.asmart_test.domain.repository.AuthTokensRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val postApi: PostApi,
    private val authTokensRepository: AuthTokensRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<PostResponse>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<PostResponse>>> = _uiState

    private var currentPage = 1
    private var isLoadingMore = false

    init {
        loadPosts(currentPage)
    }

    private fun loadPosts(page: Int) {
        viewModelScope.launch {
            try {
                isLoadingMore = true
                val posts = postApi.getPosts(page)
                val currentList = (_uiState.value as? UiState.Success)?.data.orEmpty()
                _uiState.value = UiState.Success(currentList + posts)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка загрузки")
            } finally {
                isLoadingMore = false
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingMore) return
        currentPage++
        loadPosts(currentPage)
    }

    fun refreshPage(callback: () -> Unit) {
        _uiState.value = UiState.Loading
        currentPage = 1
        loadPosts(currentPage)
        callback()
    }

    fun logout(callback: () -> Unit) {
        viewModelScope.launch {
            authTokensRepository.clearTokens()
            callback()
        }
    }
}