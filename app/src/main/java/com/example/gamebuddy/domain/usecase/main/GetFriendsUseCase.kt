package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.Friend
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetFriendsUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(): Flow<DataState<List<Friend>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()
        val token = authToken?.token ?: ""

        if (token.isBlank()) {
            Timber.e("GetFriendsUseCase: Token is null or blank.")
            throw Exception("Please login again.")
        }


//         val friends = service
//                .getFriends(token = "Bearer $token")
//                .toFriends()
//            Timber.d("GetFriendsUseCase: $friends")
//            emit(DataState.success(response = null, data = friends))

        val response = getFakeData(10)
        emit(DataState.success(response = null, data = response))


    }.catch { e ->
        Timber.e("GetFriendsUseCase Error: ${e.printStackTrace()}")
        emit(handleUseCaseException(e))
    }

    private fun getFakeData(numFriends: Int): List<Friend> {
        val friends = mutableListOf<Friend>()
        val avatarUrls = listOf(
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fapex1.jpg?alt=media&token=b69beab3-638e-431a-a05b-bb2936d40916",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fapex2.jpg?alt=media&token=a8970c67-4232-4787-bfef-eabdbb025f45",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fbattlefield2.jpg?alt=media&token=405e3800-8a17-46ee-8a5c-311858a73e05",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fbattlefield1.jpg?alt=media&token=51a32d0a-1b91-4c02-bbf1-1ddb9a82283b",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fdota1.jpg?alt=media&token=aa16f7d1-200b-4160-8076-7a04c005498e",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fdota2.jpg?alt=media&token=19d37c97-b2d1-444a-abe0-2ddc8e8e39a6",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fgta1.jpg?alt=media&token=203ddbfa-79e1-4eaf-9b2e-7fbed362b7be",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Felden-ring3.jpg?alt=media&token=e59f6193-1f23-47e7-b6bb-be6d167fb1d7",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Felden-ring4.jpg?alt=media&token=6281b601-8ff8-4293-8a94-c1e15834e2e9",
            "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Ffortnite4-special.jpg?alt=media&token=6fc3092a-890e-4966-83fa-9a3d540b36f7"
        )
        val usernames = listOf(
            "john_doe",
            "jane_doe",
            "bob_smith",
            "mary_johnson",
            "michael_brown",
            "susan_adams",
            "david_jones",
            "lisa_williams",
            "james_miller",
            "sarah_jackson"
        )

        repeat(numFriends) {
            val avatar = avatarUrls.random()
            val username = usernames.random()
            val age = (18..60).random()
            friends.add(Friend(avatar, username, age))
        }

        return friends
    }
}