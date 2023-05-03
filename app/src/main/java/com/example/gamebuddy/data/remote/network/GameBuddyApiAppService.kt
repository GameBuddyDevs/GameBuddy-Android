package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.basic.BasicResponse
import com.example.gamebuddy.data.remote.model.friends.FriendsResponse
import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.model.message.MessageResponse
import com.example.gamebuddy.data.remote.model.users.UsersResponse
import com.example.gamebuddy.data.remote.request.SendFriendRequest
import com.example.gamebuddy.data.remote.request.SendMessageRequest
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @POST("send/friend")
    @Api(ApiType.APPLICATION)
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Body userId: SendFriendRequest,
    ): BasicResponse

    @GET("get/messages/{userId}")
    @Api(ApiType.APPLICATION)
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): MessageResponse

    @POST("save/message")
    @Api(ApiType.APPLICATION)
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): BasicResponse

    @GET("get/user/info/{userId}")
    @Api(ApiType.APPLICATION)
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): ProfileResponse


}