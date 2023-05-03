package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.model.message.Conversation
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.request.SendMessageRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class SendMessageUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {

    fun execute(
        receiverId: String,
        message: String
    ): Flow<DataState<Conversation>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.toAuthToken()

        val response = service.sendMessage(
            token = "Bearer ${authToken?.token}",
            request = SendMessageRequest(
                receiverId = receiverId,
                message = message
            )
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        val formattedDateTime = formatter.format(calendar.time)
        val formattedDateTimeWithOffset = formattedDateTime.substring(0, 26) + ":" + formattedDateTime.substring(26)

        val conversation = Conversation(
            date = formattedDateTimeWithOffset,
            message = response.body.data.message,
            sender = authToken!!.pk
        )
        emit(DataState.success(response = null, data = conversation))
    }.catch {
        emit(handleUseCaseException(it))
    }
}