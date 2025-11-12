package com.example.asmart_test.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.asmart_test.domain.model.auth.AuthTokens
import com.example.asmart_test.domain.repository.AuthTokensRepository
import kotlinx.coroutines.flow.first

class AuthTokensRepositoryImpl(private val dataStore: DataStore<Preferences>) : AuthTokensRepository {

    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")

    override suspend fun getTokens(): AuthTokens {
        val prefs = dataStore.data.first()
        return AuthTokens(
            accessToken = prefs[accessKey],
            refreshToken = prefs[refreshKey]
        )
    }

    override suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit { prefs ->
            prefs[accessKey] = access
            prefs[refreshKey] = refresh
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}
