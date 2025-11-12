package com.example.asmart_test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asmart_test.domain.repository.AuthTokensRepository
import com.example.asmart_test.domain.repository.LogoutHandler
import com.example.asmart_test.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val authTokensRepository: AuthTokensRepository
) : ViewModel(), LogoutHandler {
    private val _startDestination = MutableStateFlow(Screen.Login.route)
    val startDestination: StateFlow<String> = _startDestination

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()


    init {
        viewModelScope.launch {
            _startDestination.value =
                if (authTokensRepository.getTokens().accessToken == null) Screen.Login.route else Screen.Posts.route
        }
    }

    fun navigateTo(route: String) {
        viewModelScope.launch {
            _navigationEvents.emit(route)
        }
    }

    override fun onLogout() {
        navigateTo(Screen.Login.route)
    }
}
