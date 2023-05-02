package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.request.profileRequest
import com.example.gamebuddy.domain.model.profile.profilUser
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ProfileUseCase(
    private val service:GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(): Flow<DataState<profilUser>> = flow{
        Timber.d("Profile Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val profileUser = service.getProfile(
            token = "Bearer ${authToken?.token}",
            userId = "c815aa8e-0899-426f-84bc-a41cdf216c9a"//Kaan user id
        ).toProfileUser()
        Timber.d("Profil Use Case success: ${profileUser?.username}")
        emit(DataState.success(response = null, data = profileUser))
    }.catch {
        Timber.e("Profile Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}