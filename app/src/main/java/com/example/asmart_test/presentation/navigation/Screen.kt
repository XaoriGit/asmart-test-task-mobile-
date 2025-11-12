package com.example.asmart_test.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Posts : Screen("posts")
}