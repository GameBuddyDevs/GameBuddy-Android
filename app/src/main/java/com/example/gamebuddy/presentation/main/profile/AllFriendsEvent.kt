package com.example.gamebuddy.presentation.main.profile

sealed class AllFriendsEvent {
    object GetAllFriends: AllFriendsEvent()
    object OnRemoveHeadFromQueue :AllFriendsEvent()
}