package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.setdetails.SetProfileDetailsRequest
import com.example.gamebuddy.util.DataState
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow

class CompleteProfileDetailsUseCase(
    private val service: GameBuddyApiAuthService,
    private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore,
) {
//    fun execute(
//        profileDetails: SetProfileDetailsRequest
//    ): Flow<DataState<Boolean>> = flow {
//        emit(DataState.loading())
//
//        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
//
//        val response = service.sendProfileDetails(
//            token = "Bearer ${authToken?.token}",
//            profileDetailsRequest = profileDetails
//        )
//
//
//
//
//    }
}