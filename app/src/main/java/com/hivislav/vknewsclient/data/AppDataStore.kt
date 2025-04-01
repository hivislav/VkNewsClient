package com.hivislav.vknewsclient.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(private val context: Context) {
    fun getToken(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PREFERENCES_KEY_TOKEN] }

    suspend fun updateToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCES_KEY_TOKEN] = token
        }
    }

    companion object {
        val PREFERENCES_KEY_TOKEN = stringPreferencesKey("token_pref_key")
        private const val DATA_STORE_TOKEN = "token"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_TOKEN)
    }
}
