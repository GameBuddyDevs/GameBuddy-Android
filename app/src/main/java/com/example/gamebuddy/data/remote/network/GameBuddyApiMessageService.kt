package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.chatbox.ChatBoxResponse
import com.example.gamebuddy.data.remote.model.message.MessageResponse
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GameBuddyApiMessageService {

    @GET("get/{userId}")
    @Api(ApiType.MESSAGE)
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): MessageResponse

    @GET("get/inbox")
    @Api(ApiType.MESSAGE)
    suspend fun getInbox(
        @Header("Authorization") token: String,
    ): ChatBoxResponse

}