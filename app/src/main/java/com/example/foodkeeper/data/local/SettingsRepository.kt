package com.example.foodkeeper.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val NOTIFY_DAYS_KEY = intPreferencesKey("notify_days")
    }

    val theme : Flow<String> = context.dataStore.data.map {
        it[THEME_KEY] ?: "system"
    }

    val notifyDays : Flow<Int> = context.dataStore.data.map {
        it[NOTIFY_DAYS_KEY] ?: 3
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME_KEY] = theme }
    }

    suspend fun setNotifyDays(days: Int) {
        context.dataStore.edit { it[NOTIFY_DAYS_KEY] = days }
    }

}
