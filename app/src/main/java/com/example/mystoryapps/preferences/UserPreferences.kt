package com.example.mystoryapps.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey("token")
    private val userId = stringPreferencesKey("UserId")
    private val name = stringPreferencesKey("name")

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        const val preferenceDefaultValue = "Null"
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getToken(): Flow<String> = dataStore.data.map { it[token] ?: preferenceDefaultValue }
    fun getUserId(): Flow<String> = dataStore.data.map { it[userId] ?: preferenceDefaultValue }
    fun getName(): Flow<String> = dataStore.data.map { it[name] ?: preferenceDefaultValue }

    suspend fun saveLoginSession(token: String, userId: String, name:String) {
        dataStore.edit { preferences ->
            preferences[this.token] = token
            preferences[this.userId] = userId
            preferences[this.name] = name
        }
        Log.d("UserPreferences", "Saved login session: token=$token, userId=$userId, name=$name")
    }

    suspend fun clearLoginSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}
