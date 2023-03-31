package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.forgotPassword.ForgotPasswordResponse
import com.example.gamebuddy.data.remote.model.login.LoginResponse
import com.example.gamebuddy.data.remote.model.register.RegisterResponse
import com.example.gamebuddy.data.remote.model.verify.VerifyResponse
import com.example.gamebuddy.data.remote.request.ForgotPasswordRequest
import com.example.gamebuddy.data.remote.request.LoginRequest
import com.example.gamebuddy.data.remote.request.RegisterRequest
import com.example.gamebuddy.data.remote.request.VerifyRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GameBuddyApiAuthService {


    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): RegisterResponse

    @POST("auth/sendCode")
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest,
    ): ForgotPasswordResponse

    @POST("auth/verify")
    suspend fun verify(
        @Body verifyRequest: VerifyRequest,
    ): VerifyResponse

    @POST("auth/username")
    @FormUrlEncoded
    suspend fun setUsername(
        @Field("email") userId: String,
        @Field("username") username: String,
    ): RegisterResponse

}