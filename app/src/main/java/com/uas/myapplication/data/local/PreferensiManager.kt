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

// Ekstensi untuk membuat DataStore instance
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

    // Flow realtime untuk dark mode
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DARK_MODE] ?: false
    }

    // Flow realtime untuk bahasa
    val bahasa: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_BAHASA] ?: BAHASA_INDONESIA
    }

    // Simpan preferensi dark mode
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = isDark
        }
    }

    // Simpan preferensi bahasa
    suspend fun setBahasa(kodeBahasa: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_BAHASA] = kodeBahasa
        }
    }
}