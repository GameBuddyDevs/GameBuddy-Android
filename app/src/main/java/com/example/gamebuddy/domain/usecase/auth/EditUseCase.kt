package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.model.username.UsernameResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.NewAgeRequest
import com.example.gamebuddy.data.remote.request.NewGamesRequest
import com.example.gamebuddy.data.remote.request.usernameRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class EditUseCase(
    private val service:GameBuddyApiAuthService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(
        param:String,
        username:String,
        age:Int,
        games:List<String>,
        keywords:List<String>
    ):Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        val response:UsernameResponse = when (param) {
            "username" -> {
                service.username(
                    token = "Bearer ${authToken?.token}",
                    usernameRequest = usernameRequest(
                        username = username
                    )
                )
            }
            "age" -> {
                service.newAge(
                    token = "Bearer ${authToken?.token}",
                    newAgeRequest = NewAgeRequest(
                        age = age
                    )
                )
            }
            "games" -> {
                service.newGames(
                    token = "Bearer ${authToken?.token}",
                    newGamesRequest = NewGamesRequest(
                        gamesOrKeywordsList = games
                    )
                )
            }
            else -> {
                service.newKeywords(
                    token = "Bearer ${authToken?.token}",
                    newGamesRequest = NewGamesRequest(
                        gamesOrKeywordsList = keywords
                    )
                )
            }
        }
        if (!response.status.success) {
            Timber.e("AcceptFriendsUseCase ${response.status.message}")
            throw Exception(response.status.message)
        }
        emit(DataState.success(response = null, data = true))
    }.catch { e->
        Timber.e("EditUseCase ERROR: $e")
        emit(handleUseCaseException(e))
    }
}