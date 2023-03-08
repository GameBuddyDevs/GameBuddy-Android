package com.example.gamebuddy.data.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gamebuddy.util.Constants.DATA_STORE
import kotlinx.coroutines.flow.first

class AppDataStoreManager(
    private val context: Application
) : AppDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE)

    override suspend fun setValue(
        key: String,
        value: String,
    ) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getValue(
        key: String
    ): String? {
        return context.dataStore.data.first()[stringPreferencesKey(key)]
    }


}