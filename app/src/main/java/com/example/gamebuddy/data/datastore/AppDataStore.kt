package com.example.gamebuddy.data.datastore

interface AppDataStore {

    suspend fun setValue(key: String, value: String)

    suspend fun getValue(key: String): String?
}