package com.example.appgamezone_008v_grupo14.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("gamezone_prefs")

object PrefsKeys {
    val USER_JSON = stringPreferencesKey("user_json")
    val TOKEN = stringPreferencesKey("token")
}

class Prefs(private val context: Context) {
    suspend fun saveUser(json: String) {
        context.dataStore.edit { it[PrefsKeys.USER_JSON] = json }
    }
    fun userFlow() = context.dataStore.data.map { it[PrefsKeys.USER_JSON] }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[PrefsKeys.TOKEN] = token }
    }
    fun tokenFlow() = context.dataStore.data.map { it[PrefsKeys.TOKEN] }
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
