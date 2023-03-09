package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.AuthResponse
import com.example.gamebuddy.data.remote.request.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GameBuddyApiAuthService {

//    @POST("auth/login")
//    @FormUrlEncoded
//    suspend fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): LoginResponse

//    @POST("auth/register")
//    //@FormUrlEncoded
//    suspend fun register(
//        @Field("email") email: String,
//        @Field("password") password: String,
//    ): AuthResponse

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): AuthResponse

    @POST("auth/verify")
    @FormUrlEncoded
    suspend fun verify(
        @Field("email") userId: String,
        @Field("code") code: String,
    ): AuthResponse

    @POST("auth/username")
    @FormUrlEncoded
    suspend fun setUsername(
        @Field("email") userId: String,
        @Field("username") username: String,
    ): AuthResponse

}