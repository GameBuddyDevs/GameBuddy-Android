package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.friends.FriendsResponse
import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.model.profile.ProfileResponse
import com.example.gamebuddy.data.remote.model.users.UsersResponse
import com.example.gamebuddy.data.remote.request.profileRequest
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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

    @GET("get/friends")
    @Api(ApiType.APPLICATION)
    suspend fun getFriends(
        @Header("Authorization") token: String,
        @Header("Accept-Encoding") encoding: String = "gzip, deflate, br",
        @Header("Accept") language: String = "*/*",
    ): FriendsResponse
    @GET("get/user/info/{userId}")
    @Api(ApiType.APPLICATION)
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): ProfileResponse
}