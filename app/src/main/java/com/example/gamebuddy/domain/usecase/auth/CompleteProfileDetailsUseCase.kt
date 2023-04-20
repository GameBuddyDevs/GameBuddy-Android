package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.setdetails.SetProfileDetailsRequest
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CompleteProfileDetailsUseCase(
    private val service: GameBuddyApiAuthService,
    private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore,
) {
    fun execute(
        profileDetails: SetProfileDetailsRequest
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.toAuthToken()

//        val response = service.sendProfileDetails(
//            token = "Bearer ${authToken?.token}",
//            profileDetailsRequest = profileDetails
//        )
//
//        if (!response.status.success) {
//            throw Exception(response.status.message)
//        }

        appDataStore.setValue(Constants.PROFILE_COMPLETED, "1")
        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        Timber.e("CompleteProfileUseCase: $e")
        emit(handleUseCaseException(exception = e))
    }
}