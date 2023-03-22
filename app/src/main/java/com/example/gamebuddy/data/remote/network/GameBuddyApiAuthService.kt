package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.auth.AuthResponse
import com.example.gamebuddy.data.remote.model.verify.VerifyResponse
import com.example.gamebuddy.data.remote.request.RegisterRequest
import com.example.gamebuddy.data.remote.request.VerifyRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GameBuddyApiAuthService {


    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): AuthResponse

    @POST("auth/verify")
    suspend fun verify(
        @Body verifyRequest: VerifyRequest,
    ): VerifyResponse

    @POST("auth/username")
    @FormUrlEncoded
    suspend fun setUsername(
        @Field("email") userId: String,
        @Field("username") username: String,
    ): AuthResponse

}