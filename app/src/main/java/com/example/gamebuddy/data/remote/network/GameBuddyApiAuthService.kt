package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.forgotPassword.ForgotPasswordResponse
import com.example.gamebuddy.data.remote.model.games.GameResponse
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.model.login.LoginResponse
import com.example.gamebuddy.data.remote.model.newPassword.NewPasswordResponse
import com.example.gamebuddy.data.remote.model.register.RegisterResponse
import com.example.gamebuddy.data.remote.model.setprofile.SetDetailsResponse
import com.example.gamebuddy.data.remote.model.username.UsernameResponse
import com.example.gamebuddy.data.remote.model.validate.ValidateResponse
import com.example.gamebuddy.data.remote.model.verify.VerifyResponse
import com.example.gamebuddy.data.remote.request.*
import com.example.gamebuddy.data.remote.request.setdetails.SetProfileDetailsRequest
import retrofit2.http.*

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

    @POST("auth/username")
    suspend fun username(
        @Header("Authorization") token: String,
        @Body usernameRequest: usernameRequest,
    ): UsernameResponse

    @POST("auth/verify")
    suspend fun verify(
        @Body verifyRequest: VerifyRequest,
    ): VerifyResponse

    @PUT("auth/change/pwd")
    suspend fun newPassword(
        @Header("Authorization") token: String,
        @Body newPasswordRequest: NewPasswordRequest,
    ): NewPasswordResponse

    @POST("auth/validateToken")
    suspend fun validateToken(
        @Header("Authorization") token: String,
    ): ValidateResponse

    @GET("application/get/games")
    suspend fun getGames(): GameResponse

    @GET("get/keywords")
    suspend fun getKeywords(): KeywordResponse

    @POST("auth/details")
    suspend fun sendProfileDetails(
        @Header("Authorization") token: String,
        @Body profileDetailsRequest: SetProfileDetailsRequest,
    ): SetDetailsResponse

}