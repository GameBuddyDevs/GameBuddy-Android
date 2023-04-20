package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.MessageType
import com.example.gamebuddy.util.Response
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ValidateTokenUseCase(
    private val service: GameBuddyApiAuthService,
) {

    fun execute(
        authToken: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        val response = service.validateToken(
            token = "Bearer $authToken",
        )

        if (!response.status.success) {
            throw Exception(response.status.message)
        }

        emit(
            DataState.success(
                response = Response(
                    message = "Done validating token.",
                    messageType = MessageType.None(),
                    uiComponentType = UIComponentType.None()
                ),
                data = true
            )
        )
    }.catch { e ->
        Timber.e("ValidateTokenUseCase: $e")
        emit(handleUseCaseException(exception = e))
    }
}