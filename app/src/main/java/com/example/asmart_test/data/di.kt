package com.example.asmart_test.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.asmart_test.data.api.AuthApi
import com.example.asmart_test.data.api.PostApi
import com.example.asmart_test.data.api.createHttpClient
import com.example.asmart_test.data.repository.AuthTokensRepositoryImpl
import com.example.asmart_test.domain.repository.AuthTokensRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val Context.appDataStore by preferencesDataStore(name = "app_prefs")

val dataModule = module {
    single<androidx.datastore.core.DataStore<Preferences>> {
        androidContext().appDataStore
    }
    singleOf(::AuthTokensRepositoryImpl) {bind<AuthTokensRepository>()}

    single { createHttpClient(get()) }

    singleOf(::AuthApi)
    singleOf(::PostApi)
}