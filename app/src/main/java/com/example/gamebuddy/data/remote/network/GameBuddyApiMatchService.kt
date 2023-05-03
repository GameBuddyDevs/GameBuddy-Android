package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.users.UsersResponse
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.GET
import retrofit2.http.Header

interface GameBuddyApiMatchService {

    @GET("get/recommendations")
    @Api(ApiType.MATCH)
    suspend fun getUsers(
        @Header("Authorization") token: String,
    ): UsersResponse

}