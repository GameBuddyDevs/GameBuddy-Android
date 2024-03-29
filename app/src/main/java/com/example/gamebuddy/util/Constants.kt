package com.example.gamebuddy.util

object Constants {


    // DataStore and Keys
    const val DATA_STORE: String = "app_data_store"
    const val LAST_AUTH_USER = "com.example.gamebuddy.LAST_AUTH_USER"
    const val IS_VERIFIED = "com.example.gamebuddy.IS_VERIFIED"
    const val PROFILE_COMPLETED: String = "com.example.gamebuddy.domain.usecase.auth"   // if 1 then profile is completed, else 0

    //Room
    const val ROOM_DATABASE_NAME: String = "game_buddy_database"

    // Permissions
    const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301

    // Error Messages
    const val USER_DETAILS_NOT_FINISHED: String = "User details not finished"
    const val USER_NOT_VERIFIED: String = "User not verified"

}