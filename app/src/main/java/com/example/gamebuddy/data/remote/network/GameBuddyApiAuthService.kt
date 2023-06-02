package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.forgotPassword.ForgotPasswordResponse
import com.example.gamebuddy.data.remote.model.login.LoginResponse
import com.example.gamebuddy.data.remote.model.newPassword.NewPasswordResponse
import com.example.gamebuddy.data.remote.model.register.RegisterResponse
import com.example.gamebuddy.data.remote.model.setprofile.SetDetailsResponse
import com.example.gamebuddy.data.remote.model.username.UsernameResponse
import com.example.gamebuddy.data.remote.model.validate.ValidateResponse
import com.example.gamebuddy.data.remote.model.verify.VerifyResponse
import com.example.gamebuddy.data.remote.request.*
import com.example.gamebuddy.data.remote.request.setdetails.SetProfileDetailsRequest
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.*

interface GameBuddyApiAuthService {

    @POST("login")
    @Api(ApiType.AUTH)
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @POST("register")
    @Api(ApiType.AUTH)
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): RegisterResponse

    @POST("sendCode")
    @Api(ApiType.AUTH)
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest,
    ): ForgotPasswordResponse

    @POST("username")
    @Api(ApiType.AUTH)
    suspend fun username(
        @Header("Authorization") token: String,
        @Body usernameRequest: usernameRequest,
    ): UsernameResponse

    @PUT("change/age")
    @Api(ApiType.AUTH)
    suspend fun newAge(
        @Header("Authorization") token: String,
        @Body newAgeRequest: NewAgeRequest,
    ):UsernameResponse

    @PUT("change/avatar")
    @Api(ApiType.AUTH)
    suspend fun newAvatar(
        @Header("Authorization") token: String,
        @Body newAvatarRequest: NewAvatarRequest,
    ):UsernameResponse

    @PUT("change/games")
    @Api(ApiType.AUTH)
    suspend fun newGames(
        @Header("Authorization") token: String,
        @Body newGamesRequest: NewGamesRequest,
    ):UsernameResponse

    @PUT("change/keywords")
    @Api(ApiType.AUTH)
    suspend fun newKeywords(
        @Header("Authorization") token: String,
        @Body newGamesRequest: NewGamesRequest,
    ):UsernameResponse

    @POST("verify")
    @Api(ApiType.AUTH)
    suspend fun verify(
        @Body verifyRequest: VerifyRequest,
    ): VerifyResponse

    @PUT("change/pwd")
    @Api(ApiType.AUTH)
    suspend fun newPassword(
        @Header("Authorization") token: String,
        @Body newPasswordRequest: NewPasswordRequest,
    ): NewPasswordResponse

    @POST("validateToken")
    @Api(ApiType.AUTH)
    suspend fun validateToken(
        @Header("Authorization") token: String,
    ): ValidateResponse

    @POST("details")
    @Api(ApiType.AUTH)
    suspend fun sendProfileDetails(
        @Header("Authorization") token: String,
        @Body profileDetailsRequest: SetProfileDetailsRequest,
    ): SetDetailsResponse

}