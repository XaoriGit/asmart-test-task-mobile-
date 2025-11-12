package com.example.asmart_test.presentation

import com.example.asmart_test.presentation.viewmodel.LoginViewModel
import com.example.asmart_test.presentation.viewmodel.NavigationViewModel
import com.example.asmart_test.presentation.viewmodel.PostViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::NavigationViewModel)
    viewModelOf(::PostViewModel)
}