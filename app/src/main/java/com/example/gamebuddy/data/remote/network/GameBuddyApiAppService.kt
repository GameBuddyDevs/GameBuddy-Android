package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import retrofit2.http.GET

interface GameBuddyApiAppService {

    @GET("application/get/games")
    suspend fun getGames(): GameResponse

    @GET("application/get/keywords")
    suspend fun getKeywords(): KeywordResponse

}