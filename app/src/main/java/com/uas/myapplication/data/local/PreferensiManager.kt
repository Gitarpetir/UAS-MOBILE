package com.uas.myapplication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "cairin_preferensi"
)

/**
 * Manager untuk menyimpan dan membaca preferensi pengguna:
 * - Mode Gelap (Dark Mode)
 * - Bahasa (Indonesia / English)
 */
class PreferensiManager(private val context: Context) {

    companion object {
        private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        private val KEY_BAHASA    = stringPreferencesKey("bahasa")

        const val BAHASA_INDONESIA = "id"
        const val BAHASA_INGGRIS   = "en"
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DARK_MODE] ?: false
    }

    val bahasa: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_BAHASA] ?: BAHASA_INDONESIA
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = isDark
        }
    }

    suspend fun setBahasa(kodeBahasa: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_BAHASA] = kodeBahasa
        }
    }
}