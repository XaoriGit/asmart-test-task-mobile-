package com.example.asmart_test.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.asmart_test.presentation.screen.login.LoginScreen
import com.example.asmart_test.presentation.screen.post.PostScreen
import com.example.asmart_test.presentation.viewmodel.NavigationViewModel
import org.koin.compose.koinInject

@Composable
fun Navigation(
    navigationViewModel: NavigationViewModel = koinInject()
) {
    val navController = rememberNavController()
    val startDestination by navigationViewModel.startDestination.collectAsState()

    LaunchedEffect(Unit) {
        navigationViewModel.navigationEvents.collect { route ->
            navController.navigate(route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Login.route) {
            LoginScreen() {
                navController.navigate(Screen.Posts.route)
            }
        }
        composable(Screen.Posts.route) {
            PostScreen() {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}