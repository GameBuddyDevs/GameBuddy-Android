package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.PendingFriends.PendingFriendsResponse
import com.example.gamebuddy.data.remote.model.basic.BasicResponse
import com.example.gamebuddy.data.remote.model.friends.FriendsResponse
import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.model.market.MarketResponse
import com.example.gamebuddy.data.remote.model.message.MessageResponse
import com.example.gamebuddy.data.remote.model.popularGames.PopularGamesResponse
import com.example.gamebuddy.data.remote.model.profile.ProfileResponse
import com.example.gamebuddy.data.remote.model.users.UsersResponse
import com.example.gamebuddy.data.remote.request.AcceptRejectFriendRequest
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

    @GET("get/friends")
    @Api(ApiType.APPLICATION)
    suspend fun getFriends(
        @Header("Authorization") token: String,
        @Header("Accept-Encoding") encoding: String = "gzip, deflate, br",
        @Header("Accept") language: String = "*/*",
    ): FriendsResponse

    @GET("get/friends")
    @Api(ApiType.APPLICATION)
    suspend fun getAllFriends(
        @Header("Authorization") token: String
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

    @GET("get/requests/friends")
    @Api(ApiType.APPLICATION)
    suspend fun getPendingFriends(
        @Header("Authorization") token: String,
    ): PendingFriendsResponse

    @POST("accept/friend")
    @Api(ApiType.APPLICATION)
    suspend fun acceptFriends(
        @Header("Authorization") token: String,
        @Body acceptRejectFriendRequest: AcceptRejectFriendRequest,
    ): BasicResponse


    @POST("reject/friend")
    @Api(ApiType.APPLICATION)
    suspend fun rejectFriends(
        @Header("Authorization") token: String,
        @Body acceptRejectFriendRequest: AcceptRejectFriendRequest,
    ): BasicResponse

    @POST("remove/friend")
    @Api(ApiType.APPLICATION)
    suspend fun removeFriends(
        @Header("Authorization") token: String,
        @Body acceptRejectFriendRequest: AcceptRejectFriendRequest,
    ): BasicResponse

    @GET("get/popular/games")
    @Api(ApiType.APPLICATION)
    suspend fun getPopularGames(
        @Header("Authorization") token: String,
    ): PopularGamesResponse

    @GET("get/marketplace")
    @Api(ApiType.APPLICATION)
    suspend fun getAvatars(
        @Header("Authorization") token: String,
    ): MarketResponse

    @GET("get/user/info/{userId}")
    @Api(ApiType.APPLICATION)
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): ProfileResponse


}