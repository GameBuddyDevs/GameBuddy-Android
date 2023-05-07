package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.chatbox.Inbox
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiMessageService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetChatBoxUseCase(
    private val service: GameBuddyApiMessageService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(): Flow<DataState<List<Inbox>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()
        val token = authToken?.token ?: ""

        if (token.isBlank()) {
            Timber.e("GetFriendsUseCase: Token is null or blank.")
            throw Exception("Please login again.")
        }

        val inboxList = getFakeData()
        emit(DataState.success(response = null, data = inboxList))

    }.catch { e ->
        Timber.e("GetFriendsUseCase Error: ${e.printStackTrace()}")
        emit(handleUseCaseException(e))
    }
}

private fun getFakeData(): List<Inbox> {
    return listOf(
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fapex3.jpg?alt=media&token=5906921d-2787-4049-8b46-23894dc3e8db",
            lastMessage = "Hey, how's it going?",
            lastMessageTime = "2022-03-17T09:28:00Z",
            userId = "u293481",
            username = "jessica_23"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fvalorant1.jpg?alt=media&token=2364dfb8-ec2a-479b-8d28-b63d452cbc43",
            lastMessage = "Can you send me that report?",
            lastMessageTime = "2022-03-16T16:42:00Z",
            userId = "u130487",
            username = "david_89"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fvalorant3.jpg?alt=media&token=1e73d85b-c95b-45e0-967c-b122957229d3",
            lastMessage = "Thanks for the gift!",
            lastMessageTime = "2022-03-15T12:13:00Z",
            userId = "u923081",
            username = "emily_c"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Flol6.jpg?alt=media&token=2ac4edd4-287f-4466-b11f-8b7686cad05f",
            lastMessage = "Are you coming to the meeting?",
            lastMessageTime = "2022-03-14T11:07:00Z",
            userId = "u293481",
            username = "joe_k"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fbattlefield3.jpg?alt=media&token=60980822-eeba-4b91-8e20-49e7f0c21202",
            lastMessage = "I'll be there in 5 minutes",
            lastMessageTime = "2022-03-14T09:15:00Z",
            userId = "u839102",
            username = "olivia_4"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fvalorant6.jpg?alt=media&token=02c6ab9b-c8a4-41f7-8447-62a73e036a05",
            lastMessage = "Can you help me with this problem?",
            lastMessageTime = "2022-03-13T14:32:00Z",
            userId = "u293481",
            username = "michael_w"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fdota4.jpg?alt=media&token=8a3751cd-8a40-4a5a-8017-037f7b0800a8",
            lastMessage = "Let's grab lunch tomorrow",
            lastMessageTime = "2022-03-12T12:01:00Z",
            userId = "u509293",
            username = "sara_t"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fgta1.jpg?alt=media&token=203ddbfa-79e1-4eaf-9b2e-7fbed362b7be",
            lastMessage = "What time is the event?",
            lastMessageTime = "2022-03-11T17:18:00Z",
            userId = "u293481",
            username = "peter_h"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Flol4.jpg?alt=media&token=5133570c-72ec-4553-9bbd-b2f55499e699",
            lastMessage = "Can you call me back?",
            lastMessageTime = "2022-03-10T10:47:00Z",
            userId = "u837293",
            username = "lisa_m"
        ),
        Inbox(
            avatar = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fminecraft1.jpg?alt=media&token=83ea3d49-6bc5-4707-96c0-8bc76fad8a61",
            lastMessage = "Good job on the presentation!",
            lastMessageTime = "2022-03-09T15:29:00Z",
            userId = "u293481",
            username = "jackson_p"
        )
    )
}
