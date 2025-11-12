package com.example.asmart_test

import com.example.asmart_test.data.dataModule
import com.example.asmart_test.presentation.presentationModule
import org.koin.dsl.module

val commonModule = module {
    includes(dataModule)
    includes(presentationModule)
}