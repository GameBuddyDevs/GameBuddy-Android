package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.message.Conversation
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class GetMessagesFromWebSocketUseCase(
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        userId: String, // actually user_id. It will be added to the beginning of the message to check who send the message
        message: String,
    ): Flow<DataState<Conversation>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()
        var sender = authToken?.account_pk

        if (authToken?.account_pk != userId) {
            sender = userId
        }

        Timber.d("Webscoketke sender: $sender")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        val formattedDateTime = formatter.format(calendar.time)
        val formattedDateTimeWithOffset =
            formattedDateTime.substring(0, 26) + ":" + formattedDateTime.substring(26)

        val conversation = Conversation(
            date = formattedDateTimeWithOffset,
            messageBody = message,
            sender = sender!!
        )

        emit(DataState.success(response = null, data = conversation))
    }.catch { error ->
        emit(handleUseCaseException(error))
    }
}