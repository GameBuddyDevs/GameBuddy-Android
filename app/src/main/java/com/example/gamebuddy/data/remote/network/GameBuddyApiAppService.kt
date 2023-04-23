package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.GET

interface GameBuddyApiAppService {

    @GET("get/games")
    @Api(ApiType.APPLICATION)
    suspend fun getGames(): GameResponse

    @GET("get/keywords")
    @Api(ApiType.APPLICATION)
    suspend fun getKeywords(): KeywordResponse

}