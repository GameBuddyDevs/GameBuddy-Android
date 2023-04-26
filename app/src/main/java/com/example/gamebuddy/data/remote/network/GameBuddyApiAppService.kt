package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.model.users.UsersResponse
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.GET
import retrofit2.http.Header

interface GameBuddyApiAppService {

    @GET("get/games")
    @Api(ApiType.APPLICATION)
    suspend fun getGames(): GameResponse

    @GET("get/keywords")
    @Api(ApiType.APPLICATION)
    suspend fun getKeywords(): KeywordResponse

    @GET("get/recommendations")
    @Api(ApiType.MATCH)
    suspend fun getUsers(
        @Header("Authorization") token: String,
    ): UsersResponse
}